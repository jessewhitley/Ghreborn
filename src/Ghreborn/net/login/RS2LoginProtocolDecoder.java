package Ghreborn.net.login;

import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.security.SecureRandom;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

import Ghreborn.Config;
import Ghreborn.Connection;
import Ghreborn.Server;
import Ghreborn.core.PlayerHandler;
import Ghreborn.model.players.Client;
import Ghreborn.model.players.PlayerSave;
import Ghreborn.net.PacketBuilder;
import Ghreborn.util.AlphaBeta;
import Ghreborn.util.ISAACCipher;
import Ghreborn.util.Misc;

public class RS2LoginProtocolDecoder extends FrameDecoder {

	private static final int CONNECTED = 0;
	private static final int LOGGING_IN = 1;
	private int state = CONNECTED;
	
	private static final BigInteger RSA_MODULUS = new BigInteger("109816280514803266091271275561675409853794086957235290907705788053714861514761857811226200071407957673365124797927968662769100827359622974688880030517421285399350377455233721391239278259889129250841428389282557877409924774013816688613476895549388876104536705993279999369767441960524149753005331060065591557257");

	private static final BigInteger RSA_EXPONENT = new BigInteger("108618199422167284351868206119580136679168108163967011416746335554235084808732367167238426248207983769517587311767196921708632586647321062381289673592021014136573797097570250028790492146320686552040448489508650461166183346828187076509453105562321322928820762369012723221101156047796970403600698921828777374009");

    @Override
    protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) throws Exception {
            if(!channel.isConnected()) {
                    return null;
            }
            switch (state) {
            case CONNECTED:
                    if (buffer.readableBytes() < 2)
                            return null;
                    int request = buffer.readUnsignedByte();
                    if (request != 14) {
                            System.out.println("Invalid login request: " + request);
                            channel.close();
                            return null;
                    }
                    buffer.readUnsignedByte();
                    channel.write(new PacketBuilder().putLong(0).put((byte) 0).putLong(new SecureRandom().nextLong()).toPacket());
                    state = LOGGING_IN;
                    return null;
            case LOGGING_IN:					                    
				@SuppressWarnings("unused")
				int loginType = -1, loginPacketSize = -1, loginEncryptPacketSize = -1;
				if(2 <= buffer.capacity()) {
					loginType = buffer.readByte() & 0xff; //should be 16 or 18
					loginPacketSize = buffer.readByte() & 0xff;
					loginEncryptPacketSize = loginPacketSize-(36+1+1+2);
					if(loginPacketSize <= 0 || loginEncryptPacketSize <= 0) {
						System.out.println("Zero or negative login size.");
						channel.close();
						return false;
					}
				}
				
				/**
				 * Read the magic id.
				 */
				if(loginPacketSize <= buffer.capacity()) {
					int magic = buffer.readByte() & 0xff;
					int version = buffer.readUnsignedShort();
					if(magic != 255) {
						System.out.println("Wrong magic id.");
						channel.close();
						return false;
					}
					if(version != 1) {
						//Dont Add Anything
					}
					@SuppressWarnings("unused")
					int lowMem = buffer.readByte() & 0xff;
					int resize = buffer.readByte() & 0xff;
					int width = buffer.readShort();
					int height = buffer.readShort();
					int ClientVerison = buffer.readShort();
					/**
					 * Pass the CRC keys.
					 */
					for(int i = 0; i < 9; i++) {
						buffer.readInt();
					}
					loginEncryptPacketSize--;
					if(loginEncryptPacketSize != (buffer.readByte() & 0xff)) {
						System.out.println("Encrypted size mismatch.");
						channel.close();
						return false;
					}
					
					/**
					 * Our RSA components. 
					 */
					ChannelBuffer rsaBuffer = buffer.readBytes(loginEncryptPacketSize);

					BigInteger bigInteger = new BigInteger(rsaBuffer.array());
					bigInteger = bigInteger.modPow(RSA_EXPONENT, RSA_MODULUS);
					rsaBuffer = ChannelBuffers.wrappedBuffer(bigInteger.toByteArray());
						if((rsaBuffer.readByte() & 0xff) != 10) {
							System.out.println("Encrypted id != 10.");
							channel.close();
							return false;
						}
                    final long clientHalf = rsaBuffer.readLong();
                    final long serverHalf = rsaBuffer.readLong();
                    
                    long uid = rsaBuffer.readLong();
                    final String name = Misc.formatPlayerName(Misc.getRS2String(rsaBuffer));
                    final String pass = Misc.getRS2String(rsaBuffer);


                    final int[] isaacSeed = { (int) (clientHalf >> 32), (int) clientHalf, (int) (serverHalf >> 32), (int) serverHalf };
                    final ISAACCipher inCipher = new ISAACCipher(isaacSeed);
                    for (int i = 0; i < isaacSeed.length; i++)
                            isaacSeed[i] += 50;
                    final ISAACCipher outCipher = new ISAACCipher(isaacSeed);
                    //final int version = buffer.readInt();
                    channel.getPipeline().replace("decoder", "decoder", new RS2ProtocolDecoder(inCipher));
                    return login(channel, inCipher, outCipher, version, name, pass, resize, width, height, uid, ClientVerison);
            }
            }
            return null;
            
    }
	private static Client login(Channel channel, ISAACCipher inCipher, ISAACCipher outCipher, int version, String name, String pass, int resize, int width, int height, long uid, int ClientVerison) {
		int returnCode = 2;
		if (Connection.isIpBanned(((InetSocketAddress) channel
				.getRemoteAddress()).getAddress().getHostAddress().toString())) {
			returnCode = 4;
		}
		if(ClientVerison != Config.CLIENT_VERSION) {
			returnCode = 6;
		}
		if (!name.matches("[A-Za-z0-9 ]+")) {
			returnCode = 4;
		}
		if (name.length() > 12) {
			returnCode = 8;
		}
		Client cl = new Client(channel, -1);
		cl.playerName = name;
		cl.playerName2 = cl.playerName;
		cl.playerPass = pass;
		cl.setNameAsLong(Misc.playerNameToInt64(cl.playerName));
		cl.outStream.packetEncryption = outCipher;
		cl.saveCharacter = false;
		cl.resize = resize;
		cl.width = width;
		cl.height = height; 
		cl.isActive = true;
		cl.setUniqueIdentifier(uid);
		cl.setUsernameHash(Misc.playerNameToInt64(name));
		if (Connection.isNamedBanned(cl.playerName)) {
			returnCode = 4;
		}
		if (PlayerHandler.isPlayerOn(name)) {
			returnCode = 5;
		}
		//if(!AlphaBeta.isTester(name)){
			//returnCode = 23;
		//}
		if (PlayerHandler.getPlayerCount() >= Config.MAX_PLAYERS) {
			returnCode = 7;
		}
		if (Server.UpdateServer) {
			returnCode = 14;
		}
		if (returnCode == 2) {
			int load = PlayerSave.loadGame(cl, cl.playerName, cl.playerPass);
			if (load == 0)
				cl.addStarter = true;
			cl.HasEasterItems = true;
			if (load == 3) {
				returnCode = 3;
				cl.saveFile = false;
			} else {
				for (int i = 0; i < cl.playerEquipment.length; i++) {
					if (cl.playerEquipment[i] == 0) {
						cl.playerEquipment[i] = -1;
						cl.playerEquipmentN[i] = 0;
					}
				}
				if (!Server.playerHandler.newPlayerClient(cl)) {
					returnCode = 7;
					cl.saveFile = false;
				} else {
					cl.saveFile = true;
				}
			}
		}
		if(returnCode == 2) {
			cl.saveCharacter = true;
			cl.packetType = -1;
			cl.packetSize = 0;
			final PacketBuilder bldr = new PacketBuilder();
			bldr.put((byte) 2);
			if (cl.getRights().isOwner()) {
				bldr.put((byte) 9);
			} else {
				bldr.put((byte) cl.getRights().getValue());
			}
			bldr.put((byte) 0);
			channel.write(bldr.toPacket());
		} else {
			System.out.println("returncode:" + returnCode);
			sendReturnCode(channel, returnCode);
			return null;
		}
		synchronized (PlayerHandler.lock) {
			cl.initialize();
			cl.initialized = true;
		
		}
		return cl;
	}

	public static void sendReturnCode(final Channel channel, final int code) {
		channel.write(new PacketBuilder().put((byte) code).toPacket()).addListener(new ChannelFutureListener() {
			@Override
			public void operationComplete(final ChannelFuture arg0) throws Exception {
				arg0.getChannel().close();
			}
		});
	}

}
