package Ghreborn.model.players;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import Ghreborn.Config;
import Ghreborn.clip.PathChecker;
import Ghreborn.clip.Region;
import Ghreborn.core.PlayerHandler;
import Ghreborn.event.CycleEvent;
import Ghreborn.event.CycleEventContainer;
import Ghreborn.event.CycleEventHandler;
import Ghreborn.model.Entity;
import Ghreborn.model.Location;
import Ghreborn.model.content.dailytasks.DailyTasks.PossibleTasks;
import Ghreborn.model.content.randomevents.InterfaceClicking.impl.InterfaceClickHandler;
import Ghreborn.model.content.clan.Clan;
import Ghreborn.model.content.dailytasks.TaskTypes;
import Ghreborn.model.content.teleport.Position;
import Ghreborn.model.items.Item;
import Ghreborn.model.items.ItemAssistant;
import Ghreborn.model.items.bank.Bank;
import Ghreborn.model.minigames.raids.Raids;
import Ghreborn.model.npcs.NPC;
import Ghreborn.model.npcs.NPCHandler;
import Ghreborn.model.npcs.boss.Armadyl.Armadyl;
import Ghreborn.model.npcs.boss.Bandos.Bandos;
import Ghreborn.model.npcs.boss.Kraken.Kraken;
import Ghreborn.model.npcs.boss.Saradomin.Saradomin;
import Ghreborn.model.npcs.boss.Zamorak.Zamorak;
import Ghreborn.model.npcs.boss.instances.InstancedArea;
import Ghreborn.model.npcs.boss.zulrah.Zulrah;
import Ghreborn.model.players.TitleManager.Title;
import Ghreborn.model.players.combat.Damage;
import Ghreborn.model.players.combat.Degrade;
import Ghreborn.model.players.combat.Hitmark;
import Ghreborn.model.players.skills.Prayer;
import Ghreborn.model.players.skills.agility.AgilityHandler;
import Ghreborn.model.players.skills.construction.Room;
import Ghreborn.model.players.skills.farming.Allotments;
import Ghreborn.model.players.skills.farming.Bushes;
import Ghreborn.model.players.skills.slayer.Slayer;
import Ghreborn.util.ISAACRandomGen;
import Ghreborn.util.Misc;
import Ghreborn.util.Stopwatch;
import Ghreborn.util.Stream;

public abstract class Player extends Entity {

	public ArrayList<String> killedPlayers = new ArrayList<String>();
	public ArrayList<Integer> attackedPlayers = new ArrayList<Integer>();
	private InterfaceClickHandler randomInterfaceClick = new InterfaceClickHandler(this);
	public Rights rights = Rights.PLAYER;
	public long lastButton, cerbDelay;
	public boolean ironman = false;
	private Raids raid = new Raids(this);
	public Clan clan;
	public int totalLevel;
	public ArrayList<int[]> coordinates;
	public String displayName = "notset";
	public boolean isCooking = false;
	public int raidsDamageCounters[] = new int[10];
	public boolean HasXmasItems;
	public int clickX = -1;
	public int clickY = -1;
	public int clickZ = -1;
	public int clickId = -1;
	public boolean alreadyFishing;
	public long lastDropTableSearch, lastDropTableSelected;
	public boolean playerIsCrafting;
	public boolean isFullBody = false;
	public List<Integer> searchList = new ArrayList<>();
	public boolean isFullHelm = false;
	public int dropSize = 0;
	public boolean isFullMask = false;
	public boolean insure;
	public static int spawnId;
	public int Musicvolume = 0;
	public String lastReported = "";
	public boolean isIdle = false;
	public long lastReport = 0;
	public int brightness = 0;
	public long actionTimer;
	public int votePoints;
	public String memberName = "";
	public long lastClanTeleport;
	public int lastTeleportX, lastTeleportY, lastTeleportZ;
	public int lastSent;
	public int fishingNpc = -1;
	public int[][] playerSkillProp = new int[20][15];
	public InstancedArea instancedArea;
	private PlayerAction playerAction = new PlayerAction(this);
	public Stopwatch switchDelay = new Stopwatch();
	private AgilityHandler agilityHandler = new AgilityHandler();
	public ArrayList<String> lastKilledPlayers = new ArrayList<String>();
	public boolean isRunning2 = true;
	public boolean DonatorPod = false;
	public boolean isAnimatedArmourSpawned;
	public double secondsPlayed;
	public long minutesPlayed;
	public long hoursPlayed;
	public long daysPlayed;
	public int fletchingType;

	public boolean isOperate;
	public int itemUsing;
	/**
	 * Callisto
	 */
	
	/**
	 * Variables for trading post
	 */
	public boolean inRaidsMountain() {
		return (absX > 1219 && absX < 1259 && absY > 3542 && absY < 3577);

	}
	public int[][] raidReward ={{0,0}};
	public int raidCount;

	public boolean debugMessage = false;
	public int CAST_KNOCK = 0;
	public boolean ARMADYL_INSTANCE = false;;
	public boolean BANDOS_INSTANCE = false;
	public boolean KALPHITE_INSTANCE = false;
	public boolean SARADOMIN_INSTANCE = false;
	public boolean ZAMORAK_INSTANCE = false;
	public static boolean acbSpec = false;
	public static boolean protMelee = true;
	public static boolean protMage;
	public static boolean protRange;
	public static int protMeleeTimer = 16;
	public static int protRangeTimer;
	public int lightWood;
	public static int protMageTimer;
	public int[] degradableItem = new int[Degrade.MAXIMUM_ITEMS];
	public boolean[] claimDegradableItem = new boolean[Degrade.MAXIMUM_ITEMS];
	public int prestigeLevel;
	public int prestigePoints;
	public final Stopwatch last_trap_layed = new Stopwatch();
	public int DonatorPoints = 0;
	public Stopwatch ditchDelay = new Stopwatch();
	
	public boolean isBanking = true;
	public int[] lootBag = new int[28];
	public int[] amountLoot = new int[28];
	public int itemsInLootBag;
	public long lastRequest;
	public int resize;
	public int width;
	public int titleId;
	private NPC spawnedNpc;

	/**
	 * Strings
	 */
	public String CERBERUS_ATTACK_TYPE = "";

	public String getATTACK_TYPE() {
		return CERBERUS_ATTACK_TYPE;
	}

	public void setATTACK_TYPE(String aTTACK_TYPE) {
		CERBERUS_ATTACK_TYPE = aTTACK_TYPE;
	}

	public int getTitleId() {
		return titleId;
	}
	public NPC getSpawnedNpc() {
		return spawnedNpc;
	}

public boolean HasXmasItems(){
	return HasXmasItems;
}
	public void setSpawnedNpc(NPC spawnedNpc) {
		this.spawnedNpc = spawnedNpc;
	}
	public void setTitleId(int id) {
		titleId = id;
		this.setAppearanceUpdateRequired(true);
	}
	public List<Title> unlocked = new ArrayList<>();
	/**
	 * New Daily Task Variables
	 */
	
	public PossibleTasks currentTask;
	public TaskTypes playerChoice;
	public boolean zukDead = false;
	public long infernoLeaveTimer;
	public boolean dailyEnabled = false, completedDailyTask;
	public int dailyTaskDate, totalDailyDone = 0, unfPotHerb, unfPotAmount;
	public long lastFire;
	public long lastAntifirePotion;
	public long antifireDelay, lastWheatPass,lastPickup;
	public int height;
	public boolean canUsePackets = true;;
	public int infernoWaveId;
	public int infernoWaveType;
	public long lastCast = 0;
	public int Ghostkills = 0;
	public int newDamage;
	public int smeltOre;
	private long bestZulrahTime;
	public int Druidkills = 0;
	public int Giantkills = 0;
	public int Demonkills = 0;
	public int Generalkills = 0;
	public int JDemonkills = 0;
	public int venomDamage = 0;
	public int venomMask = 0;
	public int ZULRAH_CLICKS = 0;
	public int BANDOS_CLICKS = 0;
	public int ARMADYL_CLICKS = 0;
	public int ZAMORAK_CLICKS = 0;
	public int SARADOMIN_CLICKS = 0;
	public int KRAKEN_CLICKS = 0;
	public int KALPHITE_CLICKS = 0;
	public int ARMADYL_MINION = 0;
	public int BANDOS_MINION = 0;
	public int KALPHITE_MINION = 0;
	public int SARADOMIN_MINION = 0;
	public int ZAMORAK_MINION = 0;
	long uniqueIdentifier;
public int specRestore = 0;
	public int runEnergy = 100;
	public long lastRunRecovery, lastMysteryBox;
	public long lastDamageCalculation;
	public long lastUpdate = System.currentTimeMillis();

	public boolean isRunning() {
		return isNewWalkCmdIsRunning() || (isRunning2 && isMoving);
	}
	
	public long getUniqueIdentifier() {
		return uniqueIdentifier;
	}
	public boolean inClanWars() {
		//if (Boundary.isIn(this, ClanWarsMap.getBoundaries())) {
			//return true;
		//}
		return false;
	}
	private int xOffsetWalk, yOffsetWalk;
	private boolean forceMovement, forceMovementActive = false;
	
	/**
	 * @return the forceMovement
	 */
	public boolean isForceMovementActive() {
		return forceMovementActive;
	}
	/**
	 * 0 North 1 East 2 South 3 West
	 */
//	public void setForceMovement(int xOffset, int yOffset, int speedOne, int speedTwo, String directionSet,
//			int animation) {
//		if (isForceMovementActive() || forceMovement) {
//			return;
//		}
//		stopMovement();
//		xOffsetWalk = xOffset - absX;
//		yOffsetWalk = yOffset - absY;
//		playerStandIndex = animation;
//		playerRunIndex = animation;
//		playerWalkIndex = animation;
//		forceMovementActive = true;
//		getPA().requestUpdates();
//		setAppearanceUpdateRequired(true);
//		Server.getEventHandler().submit(new Event<Player>("force_movement", this, 2) {
//
//			@Override
//			public void execute() {
//				if (attachment == null || attachment.disconnected) {
//					super.stop();
//					return;
//				}
//				attachment.updateRequired = true;
//				attachment.forceMovement = true;
//				attachment.x1 = currentX;
//				attachment.y1 = currentY;
//				attachment.x2 = currentX + xOffsetWalk;
//				attachment.y2 = currentY + yOffsetWalk;
//				attachment.speed1 = speedOne;
//				attachment.speed2 = speedTwo;
//				attachment.direction = directionSet == "NORTH" ? 0
//						: directionSet == "EAST" ? 1 : directionSet == "SOUTH" ? 2 : directionSet == "WEST" ? 3 : 0;
//				super.stop();
//			}
//		});
//		Server.getEventHandler()
//				.submit(new Event<Player>("force_movement", this, Math.abs(xOffsetWalk) + Math.abs(yOffsetWalk)) {
//
//					@Override
//					public void execute() {
//						if (attachment == null || attachment.disconnected) {
//							super.stop();
//							return;
//						}
//						forceMovementActive = false;
//						attachment.getPA().movePlayer(xOffset, yOffset, attachment.heightLevel);
//						if (attachment.playerEquipment[attachment.playerWeapon] == -1) {
//							attachment.playerStandIndex = 0x328;
//							attachment.playerTurnIndex = 0x337;
//							attachment.playerWalkIndex = 0x333;
//							attachment.playerTurn180Index = 0x334;
//							attachment.playerTurn90CWIndex = 0x335;
//							attachment.playerTurn90CCWIndex = 0x336;
//							attachment.playerRunIndex = 0x338;
//						} else {
//							attachment.getCombat().getPlayerAnimIndex(Item
//									.getItemName(attachment.playerEquipment[attachment.playerWeapon]).toLowerCase());
//						}
//						forceMovement = false;
//						super.stop();
//					}
//				});
//	}

	public boolean inEdgeBank() {
		if (absX > 3091 && absX < 3098 && absY < 3499 && absY > 3488) {
			return true;
		} else
			return false;
	}

	public boolean inCook() {
		return absX >= 3029 && absX <= 3032 && absY >= 3382 && absY <= 3384;
	}

	public boolean hasFullVoidRange() {
		return playerEquipment[playerHat] == 11664 && playerEquipment[playerLegs] == 8840
				&& playerEquipment[playerChest] == 8839 && playerEquipment[playerHands] == 8842;
	}

	public boolean hasFullEliteVoidRange() {
		return playerEquipment[playerHat] == 11664 && playerEquipment[playerLegs] == 13073
				&& playerEquipment[playerChest] == 13072 && playerEquipment[playerHands] == 8842;
	}

	public boolean hasFullVoidMage() {
		return playerEquipment[playerHat] == 11663 && playerEquipment[playerLegs] == 8840
				&& playerEquipment[playerChest] == 8839 && playerEquipment[playerHands] == 8842;
	}

	public boolean hasFullEliteVoidMage() {
		return playerEquipment[playerHat] == 11664 && playerEquipment[playerLegs] == 13073
				&& playerEquipment[playerChest] == 13072 && playerEquipment[playerHands] == 8842;
	}

	public boolean hasFullVoidMelee() {
		return playerEquipment[playerHat] == 11665 && playerEquipment[playerLegs] == 8840
				&& playerEquipment[playerChest] == 8839 && playerEquipment[playerHands] == 8842;
	}

	public boolean hasFullEliteVoidMelee() {
		return playerEquipment[playerHat] == 11664 && playerEquipment[playerLegs] == 13073
				&& playerEquipment[playerChest] == 13072 && playerEquipment[playerHands] == 8842;
	}
	private HashMap<String, ArrayList<Damage>> damageReceived = new HashMap<>();
	public void setUniqueIdentifier(long id) {
		this.uniqueIdentifier = id;
	}
	private String killer;

	public String getPlayerKiller() {
		String killer = null;
		int totalDamage = 0;
		for (Entry<String, ArrayList<Damage>> entry : damageReceived.entrySet()) {
			String player = entry.getKey();
			ArrayList<Damage> damageList = entry.getValue();
			int damage = 0;
			for (Damage d : damageList) {
				if (System.currentTimeMillis() - d.getTimestamp() < 90000) {
					damage += d.getAmount();
				}
			}
			if (totalDamage == 0 || damage > totalDamage || killer == null) {
				totalDamage = damage;
				killer = player;
			}
		}
		return killer;
	}

	public String getKiller() {
		return killer;
	}

	public void setKiller(String killer) {
		this.killer = killer;
	}
	public boolean inCamWild() {
		if (absX > 3231 && absX < 3300 && absY > 3180 && absY < 3300
				|| absX > 3120 && absX < 3300 && absY > 3236 && absY < 3300
				|| absX > 3227 && absX < 3300 && absY > 3225 && absY < 3300) {
			return true;
		}
		return false;
	}


	public boolean initialized = false, disconnected = false, ruleAgreeButton = false, isActive = false,
			isKicked = false, isSkulled = false, friendUpdate = false, newPlayer = false, hasMultiSign = false,
			saveCharacter = false, mouseButton = false, splitChat = false, chatEffects = true, acceptAid = false,
			nextDialogue = false, autocasting = false, usedSpecial = false, mageFollow = false, dbowSpec = false,
			craftingLeather = false, properLogout = false, secDbow = false, maxNextHit = false, ssSpec = false,
			vengOn = false, addStarter = false, HasEasterItems = false, accountFlagged = false, msbSpec = false;
	public int removedTasks[] = { -1, -1, -1, -1 };
	public long buySlayerTimer;
	public int ratsCaught;
	public int dropChance;
	public int dropChance2;
	public long lastBankDeposit;
	public int dropChance3;

	public boolean needsNewTask = false;
	public int slayerPoints,

			saveDelay, playerKilled, pkPoints, totalPlayerDamageDealt, killedBy, lastChatId = 1, privateChat,
			friendSlot = 0, dialogueId, randomCoffin, newLocation, specEffect, specBarId, attackLevelReq,
			defenceLevelReq, strengthLevelReq, rangeLevelReq, magicLevelReq, followId, skullTimer, votingPoints,
			nextChat = 0, talkingNpc = -1, dialogueAction = 0, autocastId, followDistance, followId2, barrageCount = 0,
			delayedDamage = 0, delayedDamage2 = 0, pcPoints = 0, magePoints = 0, desertTreasure = 0, lastArrowUsed = -1,
			clanId = -1, autoRet = 0, pcDamage = 0, xInterfaceId = 0, xRemoveId = 0, xRemoveSlot = 0, tzhaarToKill = 0,
			tzhaarKilled = 0, waveId, frozenBy = 0, poisonDamage = 0, teleAction = 0, bonusAttack = 0,
			lastNpcAttacked = 0, killCount = 0;
	public String clanName, properName;
	public boolean hasNpc = false;
	public int summonId;
	public int rememberNpcIndex;
	public int[] voidStatus = new int[5];
	public int[] itemKeptId = new int[4];
	public int[] pouches = new int[4];
	public final int[] POUCH_SIZE = { 3, 6, 9, 12 };
	public boolean[] playerSkilling = new boolean[25], invSlot = new boolean[28], equipSlot = new boolean[14],
			duelRule = new boolean[22];
	public long friends[] = new long[200];
	public double specAmount = 0;
	public double specAccuracy = 1;
	public double specDamage = 1;
	public double prayerPoint = 1.0;
	public int teleGrabItem, teleGrabX, teleGrabY, duelCount, underAttackBy, underAttackBy2, wildLevel, teleTimer,
			respawnTimer, saveTimer = 0, teleBlockLength, poisonDelay;
	public long lastPlayerMove, lastPoison, lastPoisonSip, lastvenomSip, poisonImmune, venomImmune, dfsDelay, lastVeng,
			lastYell, teleGrabDelay, protMageDelay, protMeleeDelay, protRangeDelay, lastAction, lastThieve,
			lastLockPick, alchDelay = System.currentTimeMillis(), duelDelay, teleBlockDelay, godSpellDelay, reduceStat,
			restoreStatsDelay, buryDelay, potDelay;
	public Stopwatch specDelay = new Stopwatch();
	public Stopwatch lastSpear = new Stopwatch();
	public Stopwatch lastProtItem = new Stopwatch();
	public Stopwatch singleCombatDelay = new Stopwatch();
	public Stopwatch singleCombatDelay2 = new Stopwatch();
	public boolean canChangeAppearance = false;
	public boolean mageAllowed;
	public byte poisonMask = 0;
	public Stopwatch foodDelay = new Stopwatch();
	public Stopwatch potionTimer = new Stopwatch();
	public Stopwatch logoutDelay = new Stopwatch();
	public int lastClickedItem;
	public boolean check = false;
	public boolean RANGE_ABILITY = false;
	public static boolean transforming;
	public int Donatortime = 0;
	public final int[] BOWS = { 19481, 19478, 12788, 9185, 11785, 21012, 839, 845, 847, 851, 855, 859, 841, 843, 849, 853, 857, 12424, 861, 4212, 4214, 4215, 12765, 12766, 12767, 12768, 11235, 4216,
			4217, 4218, 4219, 4220, 4221, 4222, 4223, 4734, 6724, 20997 };
	public final int[] ARROWS = { 9341, 4160, 11959, 10033, 10034, 882, 883, 884, 885, 886, 887, 888, 889, 890, 891, 892, 893, 4740, 5616, 5617, 5618, 5619, 5620, 5621, 5622, 5623, 5624, 5625,
			5626, 5627, 9139, 9140, 9141, 9142, 9143, 11875, 21316, 21326, 9144, 9145, 9240, 9241, 9242, 9243, 9244, 9245, 9286, 9287, 9288, 9289, 9290, 9291, 9292, 9293, 9294, 9295, 9296, 9297, 9298,
			9299, 9300, 9301, 9302, 9303, 9304, 9305, 9306, 11212, 11227, 11228, 11229 };
	public final int[] NO_ARROW_DROP = { 4212, 4214, 4215, 4216, 4217, 4218, 4219, 4220, 4221, 4222, 4223, 4734, 4934,
			4935, 4936, 4937 };
	public final int[] OTHER_RANGE_WEAPONS = { 863, 864, 865, 866, 867, 868, 869, 806, 807, 808, 809, 810, 811, 825,
			826, 827, 828, 829, 830, 800, 801, 802, 803, 804, 805, 6522 };

	/**
	 * Cerberus
	 */
	public long lastHeal;

	public int CAST_ROCKS = 0;
	public int CAST_GHOSTS = 0;
	public boolean alreadySpawned = false;
	public int ticks = 0;
	public int MAGIC_ATTACK = 0;
	public int lastX = absX;
	public int lastY = absY;
	public int RANGE_ATTACK = 0;
	public int MELEE_ATTACK = 0;
	public int RANDOM = 0;
	public int RANDOM_MELEE = 0;
	public boolean TICKING_DAMAGE = false;
	public boolean hasDigged = false;
	public long digging;
	public boolean canWithdraw = true;
	public int SPAWN_MINIONS = 0;
	public long SPAWN_LIZARDS;
	public long JUMP_ABILITY;
	public long hunting;
	public boolean usingMelee;
	public long abbyTime = 0;
	public int setDamage = 0;
	public boolean usingAbby;

	public boolean checkCombatDistance(Player attacker, Player target) {
		int distance = Misc.distanceBetween(attacker, target);
		int required_distance = this.getDistanceRequired();
		return (this.usingMagic || this.usingRangeWeapon || this.usingBow || this.autocasting)
				&& distance <= required_distance
						? true
						: (this.usingMelee && this.isMoving && distance <= required_distance ? true
								: distance == 1 && (this.freezeTimer <= 0 || this.getX() == target.getX()
										|| this.getY() == target.getY()));
	}

	public int getDistanceRequired() {
		return !this.usingMagic && !this.usingRangeWeapon && !usingBow && !this.autocasting ? (this.isMoving ? 3 : 1)
				: 9;
	}

	public final int[][] MAGIC_SPELLS = {
			// example {magicId, level req, animation, startGFX, projectile Id,
			// endGFX, maxhit, exp gained, rune 1, rune 1 amount, rune 2, rune 2
			// amount, rune 3, rune 3 amount, rune 4, rune 4 amount}

			// Modern Spells
			{ 1152, 1, 711, 90, 91, 92, 2, 5, 556, 1, 558, 1, 0, 0, 0, 0 }, // wind
			// strike
			{ 1154, 5, 711, 93, 94, 95, 4, 7, 555, 1, 556, 1, 558, 1, 0, 0 }, // water
			// strike
			{ 1156, 9, 711, 96, 97, 98, 6, 9, 557, 2, 556, 1, 558, 1, 0, 0 }, // earth
			// strike
			{ 1158, 13, 711, 99, 100, 101, 8, 11, 554, 3, 556, 2, 558, 1, 0, 0 }, // fire
			// strike
			{ 1160, 17, 711, 117, 118, 119, 9, 13, 556, 2, 562, 1, 0, 0, 0, 0 }, // wind
			// bolt
			{ 1163, 23, 711, 120, 121, 122, 10, 16, 556, 2, 555, 2, 562, 1, 0, 0 }, // water
			// bolt
			{ 1166, 29, 711, 123, 124, 125, 11, 20, 556, 2, 557, 3, 562, 1, 0, 0 }, // earth
			// bolt
			{ 1169, 35, 711, 126, 127, 128, 12, 22, 556, 3, 554, 4, 562, 1, 0, 0 }, // fire
			// bolt
			{ 1172, 41, 711, 132, 133, 134, 13, 25, 556, 3, 560, 1, 0, 0, 0, 0 }, // wind
			// blast
			{ 1175, 47, 711, 135, 136, 137, 14, 28, 556, 3, 555, 3, 560, 1, 0, 0 }, // water
			// blast
			{ 1177, 53, 711, 138, 139, 140, 15, 31, 556, 3, 557, 4, 560, 1, 0, 0 }, // earth
			// blast
			{ 1181, 59, 711, 129, 130, 131, 16, 35, 556, 4, 554, 5, 560, 1, 0, 0 }, // fire
			// blast
			{ 1183, 62, 711, 158, 159, 160, 17, 36, 556, 5, 565, 1, 0, 0, 0, 0 }, // wind
			// wave
			{ 1185, 65, 711, 161, 162, 163, 18, 37, 556, 5, 555, 7, 565, 1, 0, 0 }, // water
			// wave
			{ 1188, 70, 711, 164, 165, 166, 19, 40, 556, 5, 557, 7, 565, 1, 0, 0 }, // earth
			// wave
			{ 1189, 75, 711, 155, 156, 157, 20, 42, 556, 5, 554, 7, 565, 1, 0, 0 }, // fire
			// wave
			{ 1153, 3, 716, 102, 103, 104, 0, 13, 555, 3, 557, 2, 559, 1, 0, 0 }, // confuse
			{ 1157, 11, 716, 105, 106, 107, 0, 20, 555, 3, 557, 2, 559, 1, 0, 0 }, // weaken
			{ 1161, 19, 716, 108, 109, 110, 0, 29, 555, 2, 557, 3, 559, 1, 0, 0 }, // curse
			{ 1542, 66, 729, 167, 168, 169, 0, 76, 557, 5, 555, 5, 566, 1, 0, 0 }, // vulnerability
			{ 1543, 73, 729, 170, 171, 172, 0, 83, 557, 8, 555, 8, 566, 1, 0, 0 }, // enfeeble
			{ 1562, 80, 729, 173, 174, 107, 0, 90, 557, 12, 555, 12, 556, 1, 0, 0 }, // stun
			{ 1572, 20, 711, 177, 178, 181, 0, 30, 557, 3, 555, 3, 561, 2, 0, 0 }, // bind
			{ 1582, 50, 711, 177, 178, 180, 2, 60, 557, 4, 555, 4, 561, 3, 0, 0 }, // snare
			{ 1592, 79, 711, 177, 178, 179, 4, 90, 557, 5, 555, 5, 561, 4, 0, 0 }, // entangle
			{ 1171, 39, 724, 145, 146, 147, 15, 25, 556, 2, 557, 2, 562, 1, 0, 0 }, // crumble
			// undead
			{ 1539, 50, 708, 87, 88, 89, 25, 42, 554, 5, 560, 1, 0, 0, 0, 0 }, // iban
			// blast
			{ 12037, 50, 1576, 327, 328, 329, 19, 30, 560, 1, 558, 4, 0, 0, 0, 0 }, // magic
			// dart
			{ 1190, 60, 811, 0, 0, 76, 20, 60, 554, 2, 565, 2, 556, 4, 0, 0 }, // sara
			// strike
			{ 1191, 60, 811, 0, 0, 77, 20, 60, 554, 1, 565, 2, 556, 4, 0, 0 }, // cause
			// of
			// guthix
			{ 1192, 60, 811, 0, 0, 78, 20, 60, 554, 4, 565, 2, 556, 1, 0, 0 }, // flames
			// of
			// zammy
			{ 12445, 85, 1819, 0, 344, 345, 0, 65, 563, 1, 562, 1, 560, 1, 0, 0 }, // teleblock

			// Ancient Spells
			{ 12939, 50, 1978, 0, 384, 385, 13, 30, 560, 2, 562, 2, 554, 1, 556, 1 }, // smoke
			// rush
			{ 12987, 52, 1978, 0, 378, 379, 14, 31, 560, 2, 562, 2, 566, 1, 556, 1 }, // shadow
			// rush
			{ 12901, 56, 1978, 0, 0, 373, 15, 33, 560, 2, 562, 2, 565, 1, 0, 0 }, // blood
			// rush
			{ 12861, 58, 1978, 0, 360, 361, 16, 34, 560, 2, 562, 2, 555, 2, 0, 0 }, // ice
			// rush
			{ 12963, 62, 1979, 0, 0, 389, 19, 36, 560, 2, 562, 4, 556, 2, 554, 2 }, // smoke
			// burst
			{ 13011, 64, 1979, 0, 0, 382, 20, 37, 560, 2, 562, 4, 556, 2, 566, 2 }, // shadow
			// burst
			{ 12919, 68, 1979, 0, 0, 376, 21, 39, 560, 2, 562, 4, 565, 2, 0, 0 }, // blood
			// burst
			{ 12881, 70, 1979, 0, 0, 363, 22, 40, 560, 2, 562, 4, 555, 4, 0, 0 }, // ice
			// burst
			{ 12951, 74, 1978, 0, 386, 387, 23, 42, 560, 2, 554, 2, 565, 2, 556, 2 }, // smoke
			// blitz
			{ 12999, 76, 1978, 0, 380, 381, 24, 43, 560, 2, 565, 2, 556, 2, 566, 2 }, // shadow
			// blitz
			{ 12911, 80, 1978, 0, 374, 375, 25, 45, 560, 2, 565, 4, 0, 0, 0, 0 }, // blood
			// blitz
			{ 12871, 82, 1978, 366, 0, 367, 26, 46, 560, 2, 565, 2, 555, 3, 0, 0 }, // ice
			// blitz
			{ 12975, 86, 1979, 0, 0, 391, 27, 48, 560, 4, 565, 2, 556, 4, 554, 4 }, // smoke
			// barrage
			{ 13023, 88, 1979, 0, 0, 383, 28, 49, 560, 4, 565, 2, 556, 4, 566, 3 }, // shadow
			// barrage
			{ 12929, 92, 1979, 0, 0, 377, 29, 51, 560, 4, 565, 4, 566, 1, 0, 0 }, // blood
			// barrage
			{ 12891, 94, 1979, 0, 0, 369, 30, 52, 560, 4, 565, 2, 555, 6, 0, 0 }, // ice
			// barrage

			{ -1, 80, 811, 301, 0, 0, 0, 0, 554, 3, 565, 3, 556, 3, 0, 0 }, // charge
			{ -1, 21, 712, 112, 0, 0, 0, 10, 554, 3, 561, 1, 0, 0, 0, 0 }, // low
			// alch
			{ -1, 55, 713, 113, 0, 0, 0, 20, 554, 5, 561, 1, 0, 0, 0, 0 }, // high
			// alch
			{ -1, 33, 728, 142, 143, 144, 0, 35, 556, 1, 563, 1, 0, 0, 0, 0 }, // telegrab

			{ -1, 75, 1167, 1251, 1252, 1253, 29, 35, 0, 0, 0, 0, 0, 0, 0, 0 }, // trident
																				// of
																				// the
																				// seas

			{ -1, 75, 1167, 665, 1040, 1042, 32, 35, 0, 0, 0, 0, 0, 0, 0, 0 } // trident
																				// of
																				// the
																				// swamp

	};

	public boolean isAutoButton(int button) {
		for (int j = 0; j < autocastIds.length; j += 2) {
			if (autocastIds[j] == button)
				return true;
		}
		return false;
	}

	public int[] autocastIds = { 51133, 32, 51185, 33, 51091, 34, 24018, 35, 51159, 36, 51211, 37, 51111, 38, 51069, 39,
			51146, 40, 51198, 41, 51102, 42, 51058, 43, 51172, 44, 51224, 45, 51122, 46, 51080, 47, 7038, 0, 7039, 1,
			7040, 2, 7041, 3, 7042, 4, 7043, 5, 7044, 6, 7045, 7, 7046, 8, 7047, 9, 7048, 10, 7049, 11, 7050, 12, 7051,
			13, 7052, 14, 7053, 15, 47019, 27, 47020, 25, 47021, 12, 47022, 13, 47023, 14, 47024, 15 };

	// public String spellName = "Select Spell";
	public void assignAutocast(int button) {
		for (int j = 0; j < autocastIds.length; j++) {
			if (autocastIds[j] == button) {
				Client c = (Client) PlayerHandler.players[this.index];
				autocasting = true;
				autocastId = autocastIds[j + 1];
				c.getPA().sendFrame36(108, 1);
				c.setSidebarInterface(0, 328);
				// spellName = getSpellName(autocastId);
				// spellName = spellName;
				// c.getPA().sendFrame126(spellName, 354);
				c = null;
				break;
			}
		}
	}

	private long usernameHash;

	public Long getUsernameHash() {
		return usernameHash;
	}

	public void setUsernameHash(long hash) {
		this.usernameHash = hash;
	}

	public String getSpellName(int id) {
		switch (id) {
		case 0:
			return "Air Strike";
		case 1:
			return "Water Strike";
		case 2:
			return "Earth Strike";
		case 3:
			return "Fire Strike";
		case 4:
			return "Air Bolt";
		case 5:
			return "Water Bolt";
		case 6:
			return "Earth Bolt";
		case 7:
			return "Fire Bolt";
		case 8:
			return "Air Blast";
		case 9:
			return "Water Blast";
		case 10:
			return "Earth Blast";
		case 11:
			return "Fire Blast";
		case 12:
			return "Air Wave";
		case 13:
			return "Water Wave";
		case 14:
			return "Earth Wave";
		case 15:
			return "Fire Wave";
		case 32:
			return "Shadow Rush";
		case 33:
			return "Smoke Rush";
		case 34:
			return "Blood Rush";
		case 35:
			return "Ice Rush";
		case 36:
			return "Shadow Burst";
		case 37:
			return "Smoke Burst";
		case 38:
			return "Blood Burst";
		case 39:
			return "Ice Burst";
		case 40:
			return "Shadow Blitz";
		case 41:
			return "Smoke Blitz";
		case 42:
			return "Blood Blitz";
		case 43:
			return "Ice Blitz";
		case 44:
			return "Shadow Barrage";
		case 45:
			return "Smoke Barrage";
		case 46:
			return "Blood Barrage";
		case 47:
			return "Ice Barrage";
		default:
			return "Select Spell";
		}
	}

	public boolean fullVoidRange() {
		return playerEquipment[playerHat] == 11664 && playerEquipment[playerLegs] == 8840
				&& playerEquipment[playerChest] == 8839 && playerEquipment[playerHands] == 8842;
	}

	public boolean fullVoidMage() {
		return playerEquipment[playerHat] == 11663 && playerEquipment[playerLegs] == 8840
				&& playerEquipment[playerChest] == 8839 && playerEquipment[playerHands] == 8842;
	}

	public boolean fullVoidMelee() {
		return playerEquipment[playerHat] == 11665 && playerEquipment[playerLegs] == 8840
				&& playerEquipment[playerChest] == 8839 && playerEquipment[playerHands] == 8842;
	}

	public int[][] barrowsNpcs = { { 2030, 0 }, // verac
			{ 2029, 0 }, // toarg
			{ 2028, 0 }, // karil
			{ 2027, 0 }, // guthan
			{ 2026, 0 }, // dharok
			{ 2025, 0 } // ahrim
	};
	public int barrowsKillCount;

	public int reduceSpellId;
	public final int[] REDUCE_SPELL_TIME = { 250000, 250000, 250000, 500000, 500000, 500000 }; // how long does the
																								// other player stay
																								// immune to
																								// the spell
	public long[] reduceSpellDelay = new long[6];
	public final int[] REDUCE_SPELLS = { 1153, 1157, 1161, 1542, 1543, 1562 };
	public boolean[] canUseReducingSpell = { true, true, true, true, true, true };

	public int slayerTask, taskAmount;

	public int prayerId = -1;
	public int headIcon = -1;
	public int bountyIcon = -1;
	public long stopPrayerDelay, prayerDelay;
	public boolean usingPrayer;
	public final int[] PRAYER_DRAIN_RATE = { 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500,
			500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500 };
	public final int[] PRAYER_LEVEL_REQUIRED = { 1, 4, 7, 8, 9, 10, 13, 16, 19, 22, 25, 26, 27, 28, 31, 34, 37, 40, 43,
			44, 45, 46, 49, 52, 60, 70 };
	public final int[] PRAYER = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23,
			24, 25 };
	public final String[] PRAYER_NAME = { "Thick Skin", "Burst of Strength", "Clarity of Thought", "Sharp Eye",
			"Mystic Will", "Rock Skin", "Superhuman Strength", "Improved Reflexes", "Rapid Restore", "Rapid Heal",
			"Protect Item", "Hawk Eye", "Mystic Lore", "Steel Skin", "Ultimate Strength", "Incredible Reflexes",
			"Protect from Magic", "Protect from Missiles", "Protect from Melee", "Eagle Eye", "Mystic Might",
			"Retribution", "Redemption", "Smite", "Chivalry", "Piety" };
	public final int[] PRAYER_GLOW = { 83, 84, 85, 601, 602, 86, 87, 88, 89, 90, 91, 603, 604, 92, 93, 94, 95, 96, 97,
			605, 606, 98, 99, 100, 607, 608 };
	public final int[] PRAYER_HEAD_ICONS = { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 2, 1, 0,
			-1, -1, 3, 5, 4, -1, -1 };
	// {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,3,2,1,4,6,5};

	public boolean[] prayerActive = { false, false, false, false, false, false, false, false, false, false, false,
			false, false, false, false, false, false, false, false, false, false, false, false, false, false, false };

	public int duelTimer, duelTeleX, duelTeleY, duelSlot, duelSpaceReq, duelOption, duelingWith, duelStatus;
	public int headIconPk = -1, headIconHints;
	public boolean duelRequested;
	public final int[] DUEL_RULE_ID = { 1, 2, 16, 32, 64, 128, 256, 512, 1024, 4096, 8192, 16384, 32768, 65536, 131072,
			262144, 524288, 2097152, 8388608, 16777216, 67108864, 134217728 };

	public boolean doubleHit, usingSpecial, npcDroppingItems, usingRangeWeapon, usingBow, usingBallista, usingMagic, castingMagic;
	public int specMaxHitIncrease, freezeDelay, freezeTimer = -6, killerId, playerIndex, oldPlayerIndex, lastWeaponUsed,
			projectileStage, crystalBowArrowCount, playerMagicBook, teleGfx, teleEndAnimation, teleHeight, teleX, teleY,
			rangeItemUsed, killingNpcIndex, totalDamageDealt, oldNpcIndex, fightMode, attackTimer, npcIndex,
			npcClickIndex, npcType, castingSpellId, oldSpellId, spellId, hitDelay;
	public boolean magicFailed, oldMagicFailed;
	public int bowSpecShot;
	public int clickNpcType;
	public boolean inSpecMode;
	public int clickObjectType;
	public int objectId;
	public int objectX;
	public int objectY;
	public int objectXOffset;
	public int objectYOffset;
	public int objectDistance;
	public int pItemX, pItemY, pItemId;
	public boolean isMoving, walkingToItem;
	public boolean isShopping, updateShop;
	public int myShopId;
	public int tradeStatus, tradeWith;
	public boolean forcedChatUpdateRequired, inDuel, tradeAccepted, goodTrade, inTrade, tradeRequested,
			tradeResetNeeded, tradeConfirmed, tradeConfirmed2, canOffer, acceptTrade, acceptedTrade;
	public int attackAnim, animationRequest = -1, animationWaitCycles;
	public int[] playerBonus = new int[12];
	public boolean takeAsNote;
	public int combatLevel;
	public boolean saveFile = false;
	public int playerAppearance[] = new int[13];
	public int apset;
	public int actionID;
	public int wearItemTimer, wearId, wearSlot, interfaceId;
	public int XremoveSlot, XinterfaceID, XremoveID, Xamount;
	public int objRot;
	public int objId;
	/* Start of combat variables */

	public double getstr, getatt, getdef;
	public double crossbowDamage;
	public int[] clawHit = new int[4];
	public boolean[] clanWarRule = new boolean[10];
	public boolean multiAttacking, rangeEndGFXHeight, playerFletch, playerIsFletching, playerIsMining,
			playerIsFiremaking, playerIsFishing, playerIsCooking;
	public boolean below459 = true, defaultWealthTransfer, updateInventory, oldSpec, stopPlayerSkill, playerStun,
			stopPlayerPacket, usingClaws;
	public boolean playerBFishing, finishedBarbarianTraining, ignoreDefence, secondFormAutocast, usingArrows,
			usingOtherRangeWeapons, usingCross, magicDef, spellSwap, hasBankPin, recoverysSet;
	public int rangeEndGFX, boltDamage, teleotherType, playerTradeWealth, doAmount, woodcuttingTree, stageT, dfsCount,
			recoilHits, playerDialogue, clawDelay, previousDamage;
	public boolean protectItem = false;
	public boolean rebuildNPCList = false;

	public boolean WithinDistance(int objectX, int objectY, int playerX, int playerY, int distance) {
		for (int i = 0; i <= distance; i++) {
			for (int j = 0; j <= distance; j++) {
				if ((objectX + i) == playerX
						&& ((objectY + j) == playerY || (objectY - j) == playerY || objectY == playerY)) {
					return true;
				} else if ((objectX - i) == playerX
						&& ((objectY + j) == playerY || (objectY - j) == playerY || objectY == playerY)) {
					return true;
				} else if (objectX == playerX
						&& ((objectY + j) == playerY || (objectY - j) == playerY || objectY == playerY)) {
					return true;
				}
			}
		}
		return false;
	}

	/* End of combat variables */
	public int tutorial = 15;
	public boolean usingGlory = false;
	public int[] woodcut = new int[7];
	public int wcTimer = 0;
	public int[] mining = new int[3];
	public int miningTimer = 0;
	public boolean fishing = false;
	public int fishTimer = 0;
	public int smeltType; // 1 = bronze, 2 = iron, 3 = steel, 4 = gold, 5 =
							// mith, 6 = addy, 7 = rune
	public int smeltAmount;
	public int smeltTimer = 0;
	public boolean smeltInterface;
	public boolean patchCleared;
	public int[] farm = new int[2];

	public boolean antiFirePot = false;

	/**
	 * Castle Wars
	 */
	public int castleWarsTeam;
	public boolean inCwGame;
	public boolean inCwWait;

	/**
	 * Fight Pits
	 */
	public boolean inPits = false;
	public int pitsStatus = 0;

	/**
	 * SouthWest, NorthEast, SouthWest, NorthEast
	 */

	public boolean isInTut() {
		if (absX >= 2625 && absX <= 2687 && absY >= 4670 && absY <= 4735) {
			return true;
		}
		return false;
	}

	public boolean inBarrows() {
		if (absX > 3520 && absX < 3598 && absY > 9653 && absY < 9750) {
			return true;
		}
		return false;
	}

	public boolean inArea(int x, int y, int x1, int y1) {
		if (absX > x && absX < x1 && absY < y && absY > y1) {
			return true;
		}
		return false;
	}

	/**
	 * Antifire
	 */
	private int antifireTime;

	public int getAntifireTime() {
		return this.antifireTime;
	}

	public int getLocalX() {
		return getX() - 8 * getMapRegionX();
	}

	public int getLocalY() {
		return getY() - 8 * getMapRegionY();
	}

	public int setAntifireTime(int value) {
		return this.antifireTime = value;
	}

	public boolean inWild() {
		if ((absX > 2945 && absX < 3392 && absY > 3523 && absY < 3966)
				|| (absX > 3136 && absX < 3263 && absY > 10048 && absY < 10239)) {
			return true;
		}
		return false;
	}

	public boolean inEdgeville() {
		return (absX > 3040 && absX < 3200 && absY > 3460 && absY < 3519);
	}

	public boolean arenas() {
		if (absX > 3331 && absX < 3391 && absY > 3242 && absY < 3260) {
			return true;
		}
		return false;
	}

	public boolean inDuelArena() {
		if ((absX > 3322 && absX < 3394 && absY > 3195 && absY < 3291)
				|| (absX > 3311 && absX < 3323 && absY > 3223 && absY < 3248)) {
			return true;
		}
		return false;
	}
	public int getSkeletalMysticDamageCounter() {
		return raidsDamageCounters[0];
	}

	public void setSkeletalMysticDamageCounter(int damage) {
		this.raidsDamageCounters[0] = damage;
	}

	public int getTektonDamageCounter() {
		return raidsDamageCounters[1];
	}

	public void setTektonDamageCounter(int damage) {
		this.raidsDamageCounters[1] = damage;
	}

	public int getIceDemonDamageCounter() {
		return raidsDamageCounters[2];
	}

	public void setIceDemonDamageCounter(int damage) {
		this.raidsDamageCounters[2] = damage;
	}

	public int getGlodDamageCounter() {
		return raidsDamageCounters[3];
	}

	public void setGlodDamageCounter(int damage) {
		this.raidsDamageCounters[3] = damage;
	}

	public int getIceQueenDamageCounter() {
		return raidsDamageCounters[4];
	}

	public void setIceQueenDamageCounter(int damage) {
		this.raidsDamageCounters[4] = damage;
	}

	public boolean inMulti() {

		if (Boundary.isIn(this, Kraken.BOUNDARY)) {
			return true;
		}
		if (Boundary.isIn(this, Zulrah.BOUNDARY) || 

				Boundary.isIn(this, Boundary.SKOTIZO_BOSSROOM) ||
				Boundary.isIn(this, Boundary.BANDIT_CAMP_BOUNDARY) ||
				Boundary.isIn(this, Boundary.COMBAT_DUMMY) ||
				Boundary.isIn(this, Boundary.TEKTON) ||
				Boundary.isIn(this, Boundary.INFERNO) || 
				Boundary.isIn(this, Boundary.SKELETAL_MYSTICS) ||
				Boundary.isIn(this, Boundary.RAID_MAIN) ||
				Boundary.isIn(this, Boundary.ICE_DEMON) ||
				Boundary.isIn(this, Boundary.CATACOMBS_OF_KOUREND) ||
				 Boundary.isIn(this, Boundary.OLM) ||
				Boundary.isIn(this, Boundary.SAFEPKMULTI) ||
				Boundary.isIn(this, Boundary.TzHarr_City)
				|| Boundary.isIn(this, Boundary.CERBERUS_BOSSROOMS)) {
				return true;
			}
		if (Boundary.isIn(this, Saradomin.BOUNDARY)) {
			return true;
		}
		if (Boundary.isIn(this, Bandos.BOUNDARY)) {
			return true;
		}
		if (Boundary.isIn(this, Armadyl.BOUNDARY)) {
			return true;
		}
		if (Boundary.isIn(this, Zamorak.BOUNDARY)) {
			return true;
		}
		if (Boundary.isIn(this, Boundary.Train)) {
			return true;
		}
		if (Boundary.isIn(this, Boundary.CORP)) {
			return true;
		}
		if ((absX >= 3136 && absX <= 3327 && absY >= 3519 && absY <= 3607)
				|| (absX >= 3190 && absX <= 3327 && absY >= 3648 && absY <= 3839)
				|| (absX >= 3200 && absX <= 3390 && absY >= 3840 && absY <= 3967)
				|| (absX >= 2992 && absX <= 3007 && absY >= 3912 && absY <= 3967)
				|| (absX >= 2946 && absX <= 2959 && absY >= 3816 && absY <= 3831)
				|| (absX >= 3008 && absX <= 3199 && absY >= 3856 && absY <= 3903)
				|| (absX >= 3008 && absX <= 3071 && absY >= 3600 && absY <= 3711)
				|| (absX >= 3072 && absX <= 3327 && absY >= 3608 && absY <= 3647)
				|| (absX >= 2624 && absX <= 2690 && absY >= 2550 && absY <= 2619)
				|| (absX >= 2371 && absX <= 2422 && absY >= 5062 && absY <= 5117)
				|| (absX >= 2896 && absX <= 2927 && absY >= 3595 && absY <= 3630)
				|| (absX >= 2892 && absX <= 2932 && absY >= 4435 && absY <= 4464)
				|| (absX >= 2256 && absX <= 2287 && absY >= 4680 && absY <= 4711)) {
			return true;
		}
		return false;
	}

	public boolean inFightCaves() {
		return absX >= 2360 && absX <= 2445 && absY >= 5045 && absY <= 5125;
	}

	public boolean inPirateHouse() {
		return absX >= 3038 && absX <= 3044 && absY >= 3949 && absY <= 3959;
	}

	public String connectedFrom = "";
	public String globalMessage = "";

	public void initialize() {
	}

	public int waveType;
	public int[] waveInfo = new int[3];
	public int leatherType = -1;
	public boolean craftDialogue;

	public void update() {
		handler.updatePlayer(this, c.outStream);
		handler.updateNPC(this, c.outStream);
		c.flushOutStream();
	}

	public String playerName = null;
	public String playerName2 = null;
	public String playerPass = null;
	public PlayerHandler handler = null;
	public int playerItems[] = new int[28];
	public int playerItemsN[] = new int[28];
	public int bankingItems[] = new int[Config.BANK_SIZE];
	public int bankingItemsN[] = new int[Config.BANK_SIZE];

	public int bankingTab = 0;// -1 = bank closed

	public int bankItems[] = new int[Config.BANK_SIZE];
	public int bankItemsN[] = new int[Config.BANK_SIZE];
	public int bankItems1[] = new int[Config.BANK_SIZE];
	public int bankItems1N[] = new int[Config.BANK_SIZE];
	public int bankItems2[] = new int[Config.BANK_SIZE];
	public int bankItems2N[] = new int[Config.BANK_SIZE];
	public int bankItems3[] = new int[Config.BANK_SIZE];
	public int bankItems3N[] = new int[Config.BANK_SIZE];
	public int bankItems4[] = new int[Config.BANK_SIZE];
	public int bankItems4N[] = new int[Config.BANK_SIZE];
	public int bankItems5[] = new int[Config.BANK_SIZE];
	public int bankItems5N[] = new int[Config.BANK_SIZE];
	public int bankItems6[] = new int[Config.BANK_SIZE];
	public int bankItems6N[] = new int[Config.BANK_SIZE];
	public int bankItems7[] = new int[Config.BANK_SIZE];
	public int bankItems7N[] = new int[Config.BANK_SIZE];
	public int bankItems8[] = new int[Config.BANK_SIZE];
	public int bankItems8N[] = new int[Config.BANK_SIZE];
	public boolean bankNotes = false;

	public int playerStandIndex = 0x328;
	public int playerTurnIndex = 0x337;
	public int playerWalkIndex = 0x333;
	public int playerTurn180Index = 0x334;
	public int playerTurn90CWIndex = 0x335;
	public int playerTurn90CCWIndex = 0x336;
	public int playerRunIndex = 0x338;

	public int playerHat = 0;
	public int playerCape = 1;
	public int playerAmulet = 2;
	public int playerWeapon = 3;
	public int playerChest = 4;
	public int playerShield = 5;
	public int playerLegs = 7;
	public int playerHands = 9;
	public int playerFeet = 10;
	public int playerRing = 12;
	public int playerArrows = 13;

	public int playerAttack = 0;
	public int playerDefence = 1;
	public int playerStrength = 2;
	public int playerHitpoints = 3;
	public int playerRanged = 4;
	public int playerPrayer = 5;
	public int playerMagic = 6;
	public int playerCooking = 7;
	public int playerWoodcutting = 8;
	public int playerFletching = 9;
	public int playerFishing = 10;
	public int playerFiremaking = 11;
	public static int playerCrafting = 12;
	public int playerSmithing = 13;
	public int playerMining = 14;
	public int playerHerblore = 15;
	public int playerAgility = 16;
	public int playerThieving = 17;
	public int playerSlayer = 18;
	public int playerFarming = 19;
	public int playerRunecrafting = 20;
	public int playerConstruction = 22;
	public int playerHunter = 21;

	public int[] playerEquipment = new int[14];
	public int[] playerEquipmentN = new int[14];
	public int[] playerLevel = new int[25];
	public int[] playerXP = new int[25];

	public void updateshop(int i) {
		Client p = (Client) PlayerHandler.players[index];
		p.getShops().resetShop(i);
	}

	public void println_debug(String str) {
		System.out.println("[player-" + index + "]: " + str);
	}

	public void println(String str) {
		System.out.println("[player-" + index + "]: " + str);
	}

	public Player(int _playerId) {
		index = _playerId;
		rights = Rights.PLAYER;

		for (int i = 0; i < playerItems.length; i++) {
			playerItems[i] = 0;
		}
		for (int i = 0; i < playerItemsN.length; i++) {
			playerItemsN[i] = 0;
		}

		for (int i = 0; i < playerLevel.length; i++) {
			if (i == 3) {
				playerLevel[i] = 10;
			} else {
				playerLevel[i] = 1;
			}
		}

		for (int i = 0; i < playerXP.length; i++) {
			if (i == 3) {
				playerXP[i] = 1300;
			} else {
				playerXP[i] = 0;
			}
		}
		for (int i = 0; i < Config.BANK_SIZE; i++) {
			bankItems[i] = 0;
		}

		for (int i = 0; i < Config.BANK_SIZE; i++) {
			bankItemsN[i] = 0;
		}

		playerAppearance[0] = 0; // gender
		playerAppearance[1] = 7; // head
		playerAppearance[2] = 25;// Torso
		playerAppearance[3] = 29; // arms
		playerAppearance[4] = 35; // hands
		playerAppearance[5] = 39; // legs
		playerAppearance[6] = 44; // feet
		playerAppearance[7] = 14; // beard
		playerAppearance[8] = 7; // hair colour
		playerAppearance[9] = 8; // torso colour
		playerAppearance[10] = 9; // legs colour
		playerAppearance[11] = 5; // feet colour
		playerAppearance[12] = 0; // skin colour

		apset = 0;
		actionID = 0;

		playerEquipment[playerHat] = -1;
		playerEquipment[playerCape] = -1;
		playerEquipment[playerAmulet] = -1;
		playerEquipment[playerChest] = -1;
		playerEquipment[playerShield] = -1;
		playerEquipment[playerLegs] = -1;
		playerEquipment[playerHands] = -1;
		playerEquipment[playerFeet] = -1;
		playerEquipment[playerRing] = -1;
		playerEquipment[playerArrows] = -1;
		playerEquipment[playerWeapon] = -1;

		heightLevel = 0;

		teleportToX = Config.START_LOCATION_X;
		teleportToY = Config.START_LOCATION_Y;

		absX = absY = -1;
		mapRegionX = mapRegionY = -1;
		currentX = currentY = 0;
		resetWalkingQueue();
	}

	public Player getClient(int id) {
		return PlayerHandler.players[id];
	}

	public Player getClient(String name) {
		name = name.toLowerCase();
		for (int i = 0; i < Config.MAX_PLAYERS; i++) {
			if (validClient(i)) {
				Player client = getClient(i);
				if (client.playerName.toLowerCase().equalsIgnoreCase(name)) {
					return client;
				}
			}
		}
		return null;
	}

	public boolean validClient(int id) {
		if (id < 0 || id > Config.MAX_PLAYERS) {
			return false;
		}
		return validClient(getClient(id));
	}

	public boolean validClient(String name) {
		return validClient(getClient(name));
	}

	public boolean validClient(Player client) {
		return (client != null && !client.disconnected);
	}

	public void destruct() {
		playerListSize = 0;
		for (int i = 0; i < maxPlayerListSize; i++)
			playerList[i] = null;
		absX = absY = -1;
		mapRegionX = mapRegionY = -1;
		currentX = currentY = 0;
		resetWalkingQueue();
	}

	public static final int maxPlayerListSize = Config.MAX_PLAYERS;
	public Player playerList[] = new Player[maxPlayerListSize];
	public int playerListSize = 0;

	public byte playerInListBitmap[] = new byte[(Config.MAX_PLAYERS + 7) >> 3];

	public static final int maxNPCListSize = NPCHandler.maxNPCs;
	public NPC npcList[] = new NPC[maxNPCListSize];
	public int npcListSize = 0;

	public byte npcInListBitmap[] = new byte[(NPCHandler.maxNPCs + 7) >> 3];

	public boolean withinDistance(Player otherPlr) {
		if (heightLevel != otherPlr.heightLevel)
			return false;
		int deltaX = otherPlr.absX - absX, deltaY = otherPlr.absY - absY;
		return deltaX <= 15 && deltaX >= -16 && deltaY <= 15 && deltaY >= -16;
	}

	public boolean withinDistance(NPC npc) {
		if (heightLevel != npc.heightLevel)
			return false;
		if (npc.needRespawn == true)
			return false;
		int deltaX = npc.absX - absX, deltaY = npc.absY - absY;
		return deltaX <= 15 && deltaX >= -16 && deltaY <= 15 && deltaY >= -16;
	}

	public int distanceToPoint(int pointX, int pointY) {
		return (int) Math.sqrt(Math.pow(absX - pointX, 2) + Math.pow(absY - pointY, 2));
	}

	public int mapRegionX, mapRegionY;
	public int currentX, currentY;

	public int playerSE = 0x328;
	public int playerSEW = 0x333;
	public int playerSER = 0x334;

	public final int walkingQueueSize = 50;
	public int walkingQueueX[] = new int[walkingQueueSize], walkingQueueY[] = new int[walkingQueueSize];
	public int wQueueReadPtr = 0;
	public int wQueueWritePtr = 0;
	public boolean isRunning;
	public int teleportToX = -1, teleportToY = -1;

	public void resetWalkingQueue() {
		wQueueReadPtr = wQueueWritePtr = 0;

		for (int i = 0; i < walkingQueueSize; i++) {
			walkingQueueX[i] = currentX;
			walkingQueueY[i] = currentY;
		}
	}


	public void addToWalkingQueue(int x, int y) {
		// if (VirtualWorld.I(heightLevel, absX, absY, x, y, 0)) {
		int next = (wQueueWritePtr + 1) % walkingQueueSize;
		if (next == wQueueWritePtr)
			return;
		walkingQueueX[wQueueWritePtr] = x;
		walkingQueueY[wQueueWritePtr] = y;
		wQueueWritePtr = next;
		// }
	}
	public boolean goodDistance(int objectX, int objectY, int playerX, int playerY, int distance) {
		for (int i = 0; i <= distance; i++) {
			for (int j = 0; j <= distance; j++) {
				if ((objectX + i) == playerX
						&& ((objectY + j) == playerY || (objectY - j) == playerY || objectY == playerY)) {
					return true;
				} else if ((objectX - i) == playerX
						&& ((objectY + j) == playerY || (objectY - j) == playerY || objectY == playerY)) {
					return true;
				} else if (objectX == playerX
						&& ((objectY + j) == playerY || (objectY - j) == playerY || objectY == playerY)) {
					return true;
				}
			}
		}
		return false;
	}

	public int getNextWalkingDirection() {

		if (wQueueReadPtr == wQueueWritePtr)
			return -1;
		int dir;

		do {
			dir = Misc.direction(currentX, currentY, walkingQueueX[wQueueReadPtr], walkingQueueY[wQueueReadPtr]);

			if (dir == -1) {
				wQueueReadPtr = (wQueueReadPtr + 1) % walkingQueueSize;
			} else if ((dir & 1) != 0) {
				println_debug("Invalid waypoint in walking queue!");
				resetWalkingQueue();
				return -1;
			}
		} while ((dir == -1) && (wQueueReadPtr != wQueueWritePtr));
		if (dir == -1)
			return -1;
		dir >>= 1;
		currentX += Misc.directionDeltaX[dir];
		currentY += Misc.directionDeltaY[dir];

		absX += Misc.directionDeltaX[dir];
		absY += Misc.directionDeltaY[dir];
		setLocation(Location.create(absX, absY, heightLevel));
		if (isRunning()) {
			Client c = (Client) this;
			if (runEnergy > 0) {
				runEnergy--;
				c.getPA().sendFrame126(runEnergy + "%", 149);
			} else {
				isRunning = false;
				c.getPA().sendConfig(173, 0);
			}
		}
		return dir;
	}
	public void setLastLocation() {
		this.lastLocation = getLocation();
	}

	public void setLocation(Location location) {
		setLastLocation();
		this.location = location;
	}

	public Location getLocation() {
		return location;
	}
	public long plant;
	private Position walkingDestination = new Position(absX, absY, heightLevel);
	private Location location;
	private Location lastLocation;
	
	public void setWalkingDestination(Position walkingDestination) {
		this.walkingDestination = walkingDestination;
	}

	public boolean hasWalkingDestination() {
		return walkingDestination.getX() != absX || walkingDestination.getY() != absY;
	}

	private Runnable walkInteractionTask;

	public void setWalkInteractionTask(Runnable walkInteractionTask) {
		this.walkInteractionTask = walkInteractionTask;
	}

	public void submitWalkInteractionTask() {
		if (walkInteractionTask == null) {
			return;
		}
		walkInteractionTask.run();
		walkInteractionTask = null;
	}

	public Position getWalkingDestination() {
		return walkingDestination;
	}

	public boolean didTeleport = false;
	public boolean mapRegionDidChange = false;
	public int dir1 = -1, dir2 = -1;
	public boolean createItems = false;
	public int poimiX = 0, poimiY = 0;
	public int[] lastTeleport = new int[2];

	public void getNextPlayerMovement() {
		mapRegionDidChange = false;
		didTeleport = false;
		dir1 = dir2 = -1;
		if (teleportToX != -1 && teleportToY != -1) {
			mapRegionDidChange = true;
			if (mapRegionX != -1 && mapRegionY != -1) {
				int relX = teleportToX - mapRegionX * 8, relY = teleportToY - mapRegionY * 8;
				if (relX >= 2 * 8 && relX < 11 * 8 && relY >= 2 * 8 && relY < 11 * 8)
					mapRegionDidChange = false;
			}
			if (mapRegionDidChange) {
				mapRegionX = (teleportToX >> 3) - 6;
				mapRegionY = (teleportToY >> 3) - 6;
			}
			currentX = teleportToX - 8 * mapRegionX;
			currentY = teleportToY - 8 * mapRegionY;
			lastTeleport[0] = currentX;
			lastTeleport[1] = currentY;
			absX = teleportToX;
			absY = teleportToY;
			resetWalkingQueue();
			teleportToX = teleportToY = -1;
			didTeleport = true;
		} else {
			dir1 = getNextWalkingDirection();
			if (dir1 == -1) {
				setWalkingDestination(new Position(absX, absY, heightLevel));
				submitWalkInteractionTask();
				return;
			}
			if (isRunning) {
				dir2 = getNextWalkingDirection();
			}
			int deltaX = 0, deltaY = 0;
			if (currentX < 2 * 8) {
				deltaX = 4 * 8;
				mapRegionX -= 4;
				mapRegionDidChange = true;
			} else if (currentX >= 11 * 8) {
				deltaX = -4 * 8;
				mapRegionX += 4;
				mapRegionDidChange = true;
			}
			if (currentY < 2 * 8) {
				deltaY = 4 * 8;
				mapRegionY -= 4;
				mapRegionDidChange = true;
			} else if (currentY >= 11 * 8) {
				deltaY = -4 * 8;
				mapRegionY += 4;
				mapRegionDidChange = true;
			}

			if (mapRegionDidChange) {
				currentX += deltaX;
				currentY += deltaY;
				for (int i = 0; i < walkingQueueSize; i++) {
					walkingQueueX[i] += deltaX;
					walkingQueueY[i] += deltaY;
				}
			}

		}
	}

	@Override
	public Position getPosition() {
		return new Position(absX, absY, heightLevel);
	}

	public void updateThisPlayerMovement(Stream str) {
		if (mapRegionDidChange) {
			str.createFrame(73);
			str.writeWordA(mapRegionX + 6);
			str.writeWord(mapRegionY + 6);
		}

		if (didTeleport) {
			str.createFrameVarSizeWord(81);
			str.initBitAccess();
			str.writeBits(1, 1);
			str.writeBits(2, 3);
			str.writeBits(2, heightLevel);
			str.writeBits(1, 1);
			str.writeBits(1, (updateRequired) ? 1 : 0);
			str.writeBits(7, currentY);
			str.writeBits(7, currentX);
			return;
		}

		if (dir1 == -1) {
			// don't have to update the character position, because we're
			// just standing
			str.createFrameVarSizeWord(81);
			str.initBitAccess();
			isMoving = false;
			if (updateRequired) {
				// tell client there's an update block appended at the end
				str.writeBits(1, 1);
				str.writeBits(2, 0);
			} else {
				str.writeBits(1, 0);
			}
			if (DirectionCount < 50) {
				DirectionCount++;
			}
		} else {
			DirectionCount = 0;
			str.createFrameVarSizeWord(81);
			str.initBitAccess();
			str.writeBits(1, 1);

			if (dir2 == -1) {
				isMoving = true;
				str.writeBits(2, 1);
				str.writeBits(3, Misc.xlateDirectionToClient[dir1]);
				if (updateRequired)
					str.writeBits(1, 1);
				else
					str.writeBits(1, 0);
			} else {
				isMoving = true;
				str.writeBits(2, 2);
				str.writeBits(3, Misc.xlateDirectionToClient[dir1]);
				str.writeBits(3, Misc.xlateDirectionToClient[dir2]);
				if (updateRequired)
					str.writeBits(1, 1);
				else
					str.writeBits(1, 0);
			}
		}
	}

	public void updatePlayerMovement(Stream str) {
		if (dir1 == -1) {
			if (updateRequired || isChatTextUpdateRequired()) {

				str.writeBits(1, 1);
				str.writeBits(2, 0);
			} else
				str.writeBits(1, 0);
		} else if (dir2 == -1) {

			str.writeBits(1, 1);
			str.writeBits(2, 1);
			str.writeBits(3, Misc.xlateDirectionToClient[dir1]);
			str.writeBits(1, (updateRequired || isChatTextUpdateRequired()) ? 1 : 0);
		} else {

			str.writeBits(1, 1);
			str.writeBits(2, 2);
			str.writeBits(3, Misc.xlateDirectionToClient[dir1]);
			str.writeBits(3, Misc.xlateDirectionToClient[dir2]);
			str.writeBits(1, (updateRequired || isChatTextUpdateRequired()) ? 1 : 0);
		}
	}

	public byte cachedPropertiesBitmap[] = new byte[(Config.MAX_PLAYERS + 7) >> 3];

	public void addNewNPC(NPC npc, Stream str, Stream updateBlock) {
		int id = npc.index;
		npcInListBitmap[id >> 3] |= 1 << (id & 7);
		npcList[npcListSize++] = npc;

		str.writeBits(14, id);

		int z = npc.absY - absY;
		if (z < 0)
			z += 32;
		str.writeBits(5, z);
		z = npc.absX - absX;
		if (z < 0)
			z += 32;
		str.writeBits(5, z);

		str.writeBits(1, 0);
		str.writeBits(14, npc.npcType);

		boolean savedUpdateRequired = npc.updateRequired;
		npc.updateRequired = true;
		npc.appendNPCUpdateBlock(updateBlock);
		npc.updateRequired = savedUpdateRequired;
		str.writeBits(1, 1);
	}

	public void addNewPlayer(Player plr, Stream str, Stream updateBlock) {
		// synchronized(this) {
		if (playerListSize >= 255) {
			return;
		}
		int id = plr.index;
		playerInListBitmap[id >> 3] |= 1 << (id & 7);
		playerList[playerListSize++] = plr;
		str.writeBits(11, id);
		str.writeBits(1, 1);
		boolean savedFlag = plr.isAppearanceUpdateRequired();
		boolean savedUpdateRequired = plr.updateRequired;
		plr.setAppearanceUpdateRequired(true);
		plr.updateRequired = true;
		plr.appendPlayerUpdateBlock(updateBlock);
		plr.setAppearanceUpdateRequired(savedFlag);
		plr.updateRequired = savedUpdateRequired;
		str.writeBits(1, 1);
		int z = plr.absY - absY;
		if (z < 0)
			z += 32;
		str.writeBits(5, z);
		z = plr.absX - absX;
		if (z < 0)
			z += 32;
		str.writeBits(5, z);
	}

	public int DirectionCount = 0;
	public boolean appearanceUpdateRequired = true;
	public boolean isNpc = false;
	public int npcId2 = 0;
	public int playerIsVisible = 1; // Gnarly: Player is visible, 1 = true, 0 = false.
	private Hitmark hitmark = null;
	private Hitmark secondHitmark = null;
	protected static Stream playerProps;
	static {
		playerProps = new Stream(new byte[100]);
	}

	protected void appendPlayerAppearance(Stream str) {
		playerProps.currentOffset = 0;

		playerProps.writeByte(playerAppearance[0]);

		playerProps.writeByte(headIcon);
		playerProps.writeByte(headIconPk);
		// playerProps.writeByte(headIconHints);
		// playerProps.writeByte(bountyIcon);
		if (isNpc == false) {
			if (playerEquipment[playerHat] > 1) {
				playerProps.writeWord(0x200 + playerEquipment[playerHat]);
			} else {
				playerProps.writeByte(0);
			}

			if (playerEquipment[playerCape] > 1) {
				playerProps.writeWord(0x200 + playerEquipment[playerCape]);
			} else {
				playerProps.writeByte(0);
			}

			if (playerEquipment[playerAmulet] > 1) {
				playerProps.writeWord(0x200 + playerEquipment[playerAmulet]);
			} else {
				playerProps.writeByte(0);
			}

			if (playerEquipment[playerWeapon] > 1) {
				playerProps.writeWord(0x200 + playerEquipment[playerWeapon]);
			} else {
				playerProps.writeByte(0);
			}

			if (playerEquipment[playerChest] > 1) {
				playerProps.writeWord(0x200 + playerEquipment[playerChest]);
			} else {
				playerProps.writeWord(0x100 + playerAppearance[2]);
			}

			if (playerEquipment[playerShield] > 1) {
				playerProps.writeWord(0x200 + playerEquipment[playerShield]);
			} else {
				playerProps.writeByte(0);
			}

			if (!Item.isPlate(playerEquipment[playerChest])) {
				playerProps.writeWord(0x100 + playerAppearance[3]);
			} else {
				playerProps.writeByte(0);
			}

			if (playerEquipment[playerLegs] > 1) {
				playerProps.writeWord(0x200 + playerEquipment[playerLegs]);
			} else {
				playerProps.writeWord(0x100 + playerAppearance[5]);
			}

			if (!Item.isFullHelm(playerEquipment[playerHat]) && !Item.isFullMask(playerEquipment[playerHat])) {
				playerProps.writeWord(0x100 + playerAppearance[1]);
			} else {
				playerProps.writeByte(0);
			}

			if (playerEquipment[playerHands] > 1) {
				playerProps.writeWord(0x200 + playerEquipment[playerHands]);
			} else {
				playerProps.writeWord(0x100 + playerAppearance[4]);
			}

			if (playerEquipment[playerFeet] > 1) {
				playerProps.writeWord(0x200 + playerEquipment[playerFeet]);
			} else {
				playerProps.writeWord(0x100 + playerAppearance[6]);
			}

			if (playerAppearance[0] != 1 && !Item.isFullMask(playerEquipment[playerHat])) {
				playerProps.writeWord(0x100 + playerAppearance[7]);
			} else {
				playerProps.writeByte(0);
			}
		} else {
			playerProps.writeWord(-1);
			playerProps.writeWord(npcId2);
		}
		playerProps.writeByte(playerAppearance[8]);
		playerProps.writeByte(playerAppearance[9]);
		playerProps.writeByte(playerAppearance[10]);
		playerProps.writeByte(playerAppearance[11]);
		playerProps.writeByte(playerAppearance[12]);
		playerProps.writeWord(playerStandIndex); // standAnimIndex
		playerProps.writeWord(playerTurnIndex); // standTurnAnimIndex
		playerProps.writeWord(playerWalkIndex); // walkAnimIndex
		playerProps.writeWord(playerTurn180Index); // turn180AnimIndex
		playerProps.writeWord(playerTurn90CWIndex); // turn90CWAnimIndex
		playerProps.writeWord(playerTurn90CCWIndex); // turn90CCWAnimIndex
		playerProps.writeWord(playerRunIndex); // runAnimIndex
		playerProps.writeQWord(Misc.playerNameToInt64(displayName));
		combatLevel = calculateCombatLevel();
		playerProps.writeByte(combatLevel); // combat level
		playerProps.writeWord(getTitleId());
		//playerProps.writeWord(0);
		playerProps.writeWord(playerIsVisible);
		str.writeByteC(playerProps.currentOffset);
		str.writeBytes(playerProps.buffer, playerProps.currentOffset, 0);
	}

	public int calculateCombatLevel() {
		int j = getLevelForXP(playerXP[playerAttack]);
		int k = getLevelForXP(playerXP[playerDefence]);
		int l = getLevelForXP(playerXP[playerStrength]);
		int i1 = getLevelForXP(playerXP[playerHitpoints]);
		int j1 = getLevelForXP(playerXP[playerPrayer]);
		int k1 = getLevelForXP(playerXP[playerRanged]);
		int l1 = getLevelForXP(playerXP[playerMagic]);
		int combatLevel = (int) (((k + i1) + Math.floor(j1 / 2)) * 0.25D) + 1;
		double d = (j + l) * 0.32500000000000001D;
		double d1 = Math.floor(k1 * 1.5D) * 0.32500000000000001D;
		double d2 = Math.floor(l1 * 1.5D) * 0.32500000000000001D;
		if (d >= d1 && d >= d2) {
			combatLevel += d;
		} else if (d1 >= d && d1 >= d2) {
			combatLevel += d1;
		} else if (d2 >= d && d2 >= d1) {
			combatLevel += d2;
		}
		return combatLevel;
	}

	public static int getLevelForXP(int exp) {
		if (exp > 13034430) {
			return 99;
		} else {
			int points = 0;
			for (int lvl = 1; lvl <= 99; ++lvl) {
				points = (int) (points
						+ Math.floor(lvl + 300.0D * Math.pow(2.0D, lvl / 7.0D)));
				int var5 = (int) Math.floor(points / 4);
				if (var5 >= exp) {
					return lvl;
				}
			}

			return 99;
		}
	}

	public Room toReplace, replaceWith;
	private boolean chatTextUpdateRequired = false;
	private byte chatText[] = new byte[4096];
	private byte chatTextSize = 0;
	private int chatTextColor = 0;
	private int chatTextEffects = 0;

	protected void appendPlayerChatText(Stream str) {
		// synchronized(this) {
		str.writeWordBigEndian(((getChatTextColor() & 0xFF) << 8) + (getChatTextEffects() & 0xFF));
		str.writeByte(rights.getValue());
		str.writeByteC(getChatTextSize());
		str.writeBytes_reverse(getChatText(), getChatTextSize(), 0);

	}

	public void appendForcedChat(Stream str) {
		str.writeString(forcedText);
	}

	/**
	 * Graphics
	 **/

	public void appendMask100Update(Stream str) {
		// synchronized(this) {
		str.writeWordBigEndian(gfxVar1);
		str.writeDWord(gfxVar2);

	}

	public boolean wearing2h() {
		Client c = (Client) this;
		String s = c.getItems().getItemName(c.playerEquipment[c.playerWeapon]);
		if (s.contains("2h"))
			return true;
		else if (s.contains("godsword"))
			return true;
		return false;
	}

	/**
	 * Animations
	 **/
	public void startAnimation(int animId) {
		if (wearing2h() && animId == 829)
			return;
		animationRequest = animId;
		animationWaitCycles = 0;
		updateRequired = true;
	}

	public void appendAnimationRequest(Stream str) {
		// synchronized(this) {
		str.writeWordBigEndian((animId == -1) ? 65535 : animId);
		str.writeByteC(animDelay);

	}

	/**
	 * Face Update
	 **/

	public void appendFaceUpdate(Stream str) {
		str.writeWordBigEndian(face);
	}

	public int FocusPointX = -1;
	public int FocusPointY = -1;

	public void turnPlayerTo(int pointX, int pointY) {
		FocusPointX = 2 * pointX + 1;
		FocusPointY = 2 * pointY + 1;
		updateRequired = true;
	}

	private void appendSetFocusDestination(Stream str) {
		// synchronized(this) {
		str.writeWordBigEndianA(faceX);
		str.writeWordBigEndian(faceY);

	}

	/**
	 * Hit Update
	 **/

	protected void appendHitUpdate(Stream str) {
		str.writeByte(hitDiff);
		if (hitmark == null) {
			str.writeByteA(0);
		} else {
			str.writeByteA(hitmark.getId());
		}
		if (playerLevel[3] <= 0) {
			playerLevel[3] = 0;
			isDead = true;
		}
		str.writeByteC(playerLevel[3]);
		str.writeByte(getLevelForXP(playerXP[3]));
	}

	protected void appendHitUpdate2(Stream str) {
		str.writeByte(hitDiff2);
		if (secondHitmark == null) {
			str.writeByteS(0);
		} else {
			str.writeByteS(secondHitmark.getId());
		}
		if (playerLevel[3] <= 0) {
			playerLevel[3] = 0;
			isDead = true;
		}
		str.writeByte(playerLevel[3]);
		str.writeByteC(getLevelForXP(playerXP[3]));

	}

	public boolean forceMovementUpdateRequired;

	public void setForceMovementRequired(boolean required) {
		forceMovementUpdateRequired = required;
	}

	public void appendForceMovement(Stream str) {
		str.writeByteS(absX);
		str.writeByteS(absY);
		str.writeByteS(absX);
		str.writeByteS(absY + 5);
		str.writeWordBigEndianA(4);
		str.writeWordA(100);
		str.writeByteS(2);
	}

	private int x1 = -1;
	private int y1 = -1;
	private int x2 = -1;
	private int y2 = -1;
	private int speed1 = -1;
	public int makeTimes;
	public int event;
	private int speed2 = -1;
	private int direction = -1;
	public boolean canWalk = true;

	/**
	 * Mutates the current hitmark to be that of the new hitmark. This is
	 * commonly done in battle.
	 * 
	 * @param hitmark
	 *            the hitmark
	 */
	public void setHitmark(Hitmark hitmark) {
		this.hitmark = hitmark;
	}

	/**
	 * Mutates the first and second hitmark. This is commonly done in battle.
	 * 
	 * @param hitmark
	 *            the first hitmark
	 * @param secondHitmark
	 *            the second hitmark
	 */
	public void setHitmarks(Hitmark hitmark, Hitmark secondHitmark) {
		this.hitmark = hitmark;
		this.secondHitmark = secondHitmark;
	}
	/**
	 * Creates a force-movement update block. (0x400)
	 * 
	 * @param x2
	 * @param y2
	 * @param x1
	 * @param y1
	 * @param speed1
	 * @param speed2
	 * @param direction
	 * @param ticks
	 */

	public boolean canWalk() {
		return canWalk;
	}

	public void setCanWalk(boolean canWalk) {
		this.canWalk = canWalk;
	}

	public void setForceMovement(final int x2, final int y2, boolean x1, boolean y1, final int speed1, final int speed2,
			final int direction, final int ticks) {
		setCanWalk(false);
		this.x1 = getPosition().getLocalX();
		this.y1 = getPosition().getLocalY();
		this.x2 = x1 ? getPosition().getLocalX() + x2 : getPosition().getLocalX() - x2;
		this.y2 = y1 ? getPosition().getLocalY() + y2 : getPosition().getLocalY() - y2;
		this.speed1 = speed1;
		this.speed2 = speed2;
		this.direction = direction;
		updateRequired = true;
		forceMovementUpdateRequired = true;
		CycleEventHandler.getSingleton().addEvent(this, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				absX = (x1 ? absX + x2 : absX - x2);
				absY = (y1 ? absY + y2 : absY - y2);
				container.stop();
			}
		}, ticks);
		CycleEventHandler.getSingleton().addEvent(this, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				getPA().movePlayer(getPosition().getX(), getPosition().getY(), getPosition().getZ());
				setForceMovementRequired(false);
				setCanWalk(true);
				container.stop();
			}
		}, ticks + 1);
	}

	public void appendMask400Update(Stream str) {
		str.writeByteS(x1);
		str.writeByteS(y1);
		str.writeByteS(x2);
		str.writeByteS(y2);
		str.writeWordBigEndianA(speed1);
		str.writeWordA(speed2);
		str.writeByteS(direction);
	}

	public void appendPlayerUpdateBlock(Stream str) {
		// synchronized(this) {
		if (!updateRequired && !isChatTextUpdateRequired())
			return; // nothing required
		int updateMask = 0;
		if (forceMovementUpdateRequired) {
			updateMask |= 0x400;
		}
		if (gfxUpdateRequired) {
			updateMask |= 0x100;
		}
		if (animUpdateRequired) {
			updateMask |= 8;
		}
		if (forcedChatUpdateRequired) {
			updateMask |= 4;
		}
		if (isChatTextUpdateRequired()) {
			updateMask |= 0x80;
		}
		if (isAppearanceUpdateRequired()) {
			updateMask |= 0x10;
		}
		if (faceUpdateRequired) {
			updateMask |= 1;
		}
		if (facePositionUpdateRequired) {
			updateMask |= 2;
		}
		if (hitUpdateRequired) {
			updateMask |= 0x20;
		}

		if (hitUpdateRequired2) {
			updateMask |= 0x200;
		}

		if (updateMask >= 0x100) {
			updateMask |= 0x40;
			str.writeByte(updateMask & 0xFF);
			str.writeByte(updateMask >> 8);
		} else {
			str.writeByte(updateMask);
		}
		if (forceMovementUpdateRequired) {
			appendMask400Update(str);
		}
		if (gfxUpdateRequired) {
			appendMask100Update(str);
		}
		if (animUpdateRequired) {
			appendAnimationRequest(str);
		}
		if (forcedChatUpdateRequired) {
			appendForcedChat(str);
		}
		if (isChatTextUpdateRequired()) {
			appendPlayerChatText(str);
		}
		if (faceUpdateRequired) {
			appendFaceUpdate(str);
		}
		if (isAppearanceUpdateRequired()) {
			appendPlayerAppearance(str);
		}
		if (facePositionUpdateRequired) {
			appendSetFocusDestination(str);
		}
		if (hitUpdateRequired) {
			appendHitUpdate(str);
		}
		if (hitUpdateRequired2) {
			appendHitUpdate2(str);
		}

	}

	public boolean inConstruction() {
		if ((absX >= 16 && absX <= 55 && absY >= 16 && absY <= 55)) {
			return true;
		}
		return false;
	}

	public void stopMovement() {
		if (lastTeleport[0] == currentX && lastTeleport[1] == currentY) {
			newWalkCmdSteps = 0;
			getNewWalkCmdX()[0] = getNewWalkCmdY()[0] = travelBackX[0] = travelBackY[0] = 0;
			return;
		}
		if (teleportToX <= 0 && teleportToY <= 0) {
			teleportToX = absX;
			teleportToY = absY;
		}
		newWalkCmdSteps = 0;
		getNewWalkCmdX()[0] = getNewWalkCmdY()[0] = travelBackX[0] = travelBackY[0] = 0;
		getNextPlayerMovement();
	}
	public int[] runes = new int[3];
	public int[] runeAmount = new int[3];
	public int hits = 0;
	public int runesInPouch;

	private int newWalkCmdX[] = new int[walkingQueueSize];
	private int newWalkCmdY[] = new int[walkingQueueSize];
	public int newWalkCmdSteps = 0;
	private boolean newWalkCmdIsRunning = false;
	protected int travelBackX[] = new int[walkingQueueSize];
	protected int travelBackY[] = new int[walkingQueueSize];
	protected int numTravelBackSteps = 0;

	public void preProcessing() {
		newWalkCmdSteps = 0;
	}

	public abstract void process();


	public void postProcessing() {
		if (newWalkCmdSteps > 0) {
			int firstX = getNewWalkCmdX()[0], firstY = getNewWalkCmdY()[0];

			int lastDir = 0;
			boolean found = false;
			numTravelBackSteps = 0;
			int ptr = wQueueReadPtr;
			int dir = Misc.direction(currentX, currentY, firstX, firstY);
			if (dir != -1 && (dir & 1) != 0) {
				do {
					lastDir = dir;
					if (--ptr < 0)
						ptr = walkingQueueSize - 1;

					travelBackX[numTravelBackSteps] = walkingQueueX[ptr];
					travelBackY[numTravelBackSteps++] = walkingQueueY[ptr];
					dir = Misc.direction(walkingQueueX[ptr], walkingQueueY[ptr], firstX, firstY);
					if (lastDir != dir) {
						found = true;
						break;
					}

				} while (ptr != wQueueWritePtr);
			} else
				found = true;

			if (!found)
				println_debug("Fatal: couldn't find connection vertex! Dropping packet.");
			else {
				wQueueWritePtr = wQueueReadPtr;

				addToWalkingQueue(currentX, currentY);

				if (dir != -1 && (dir & 1) != 0) {

					for (int i = 0; i < numTravelBackSteps - 1; i++) {
						addToWalkingQueue(travelBackX[i], travelBackY[i]);
					}
					int wayPointX2 = travelBackX[numTravelBackSteps - 1],
							wayPointY2 = travelBackY[numTravelBackSteps - 1];
					int wayPointX1, wayPointY1;
					if (numTravelBackSteps == 1) {
						wayPointX1 = currentX;
						wayPointY1 = currentY;
					} else {
						wayPointX1 = travelBackX[numTravelBackSteps - 2];
						wayPointY1 = travelBackY[numTravelBackSteps - 2];
					}

					dir = Misc.direction(wayPointX1, wayPointY1, wayPointX2, wayPointY2);
					if (dir == -1 || (dir & 1) != 0) {
						println_debug("Fatal: The walking queue is corrupt! wp1=(" + wayPointX1 + ", " + wayPointY1
								+ "), " + "wp2=(" + wayPointX2 + ", " + wayPointY2 + ")");
					} else {
						dir >>= 1;
						found = false;
						int x = wayPointX1, y = wayPointY1;
						while (x != wayPointX2 || y != wayPointY2) {
							x += Misc.directionDeltaX[dir];
							y += Misc.directionDeltaY[dir];
							if ((Misc.direction(x, y, firstX, firstY) & 1) == 0) {
								found = true;
								break;
							}
						}
						if (!found) {
							println_debug("Fatal: Internal error: unable to determine connection vertex!" + "  wp1=("
									+ wayPointX1 + ", " + wayPointY1 + "), wp2=(" + wayPointX2 + ", " + wayPointY2
									+ "), " + "first=(" + firstX + ", " + firstY + ")");
						} else
							addToWalkingQueue(wayPointX1, wayPointY1);
					}
				} else {
					for (int i = 0; i < numTravelBackSteps; i++) {
						addToWalkingQueue(travelBackX[i], travelBackY[i]);
					}
				}

				for (int i = 0; i < newWalkCmdSteps; i++) {
					addToWalkingQueue(getNewWalkCmdX()[i], getNewWalkCmdY()[i]);
				}

			}

			isRunning = isNewWalkCmdIsRunning() || isRunning2;
		}
	}
	public int getMapRegionX() {
		return mapRegionX;
	}

	public int getRegionX() {
		return (absX >> 6);
	}

	public int getRegionY() {
		return (absY >> 6);
	}

	public int getRegionID() {
		return ((getLocalX() << 8) + getLocalY());
	}

	public int getMapRegionY() {
		return mapRegionY;
	}

	public int getId() {
		return index;
	}

	public boolean inPcBoat() {
		return absX >= 2660 && absX <= 2663 && absY >= 2638 && absY <= 2643;
	}

	public boolean inPcGame() {
		return absX >= 2624 && absX <= 2690 && absY >= 2550 && absY <= 2619;
	}

	public void setHitDiff(int hitDiff) {
		this.hitDiff = hitDiff;
	}

	public void setHitDiff2(int hitDiff2) {
		this.hitDiff2 = hitDiff2;
	}

	public int getHitDiff() {
		return hitDiff;
	}

	public void setHitUpdateRequired(boolean hitUpdateRequired) {
		this.hitUpdateRequired = hitUpdateRequired;
	}

	public void setHitUpdateRequired2(boolean hitUpdateRequired2) {
		this.hitUpdateRequired2 = hitUpdateRequired2;
	}

	public boolean isHitUpdateRequired() {
		return hitUpdateRequired;
	}

	public boolean getHitUpdateRequired() {
		return hitUpdateRequired;
	}

	public boolean getHitUpdateRequired2() {
		return hitUpdateRequired2;
	}

	public void setAppearanceUpdateRequired(boolean appearanceUpdateRequired) {
		this.appearanceUpdateRequired = appearanceUpdateRequired;
	}

	public boolean isAppearanceUpdateRequired() {
		return appearanceUpdateRequired;
	}

	public void setChatTextEffects(int chatTextEffects) {
		this.chatTextEffects = chatTextEffects;
	}

	public int getChatTextEffects() {
		return chatTextEffects;
	}

	public void setChatTextSize(byte chatTextSize) {
		this.chatTextSize = chatTextSize;
	}

	public byte getChatTextSize() {
		return chatTextSize;
	}

	public void setChatTextUpdateRequired(boolean chatTextUpdateRequired) {
		this.chatTextUpdateRequired = chatTextUpdateRequired;
	}

	public boolean isChatTextUpdateRequired() {
		return chatTextUpdateRequired;
	}

	public void setChatText(byte chatText[]) {
		this.chatText = chatText;
	}

	public byte[] getChatText() {
		return chatText;
	}

	public void setChatTextColor(int chatTextColor) {
		this.chatTextColor = chatTextColor;
	}

	public int getChatTextColor() {
		return chatTextColor;
	}

	public void setNewWalkCmdX(int newWalkCmdX[]) {
		this.newWalkCmdX = newWalkCmdX;
	}

	public int[] getNewWalkCmdX() {
		return newWalkCmdX;
	}

	public void setNewWalkCmdY(int newWalkCmdY[]) {
		this.newWalkCmdY = newWalkCmdY;
	}

	public int[] getNewWalkCmdY() {
		return newWalkCmdY;
	}

	public void setNewWalkCmdIsRunning(boolean newWalkCmdIsRunning) {
		this.newWalkCmdIsRunning = newWalkCmdIsRunning;
	}

	public boolean isNewWalkCmdIsRunning() {
		return newWalkCmdIsRunning;
	}

	public void setInStreamDecryption(ISAACRandomGen inStreamDecryption) {
	}

	public void setOutStreamDecryption(ISAACRandomGen outStreamDecryption) {
	}

	public boolean samePlayer() {
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (j == index)
				continue;
			if (PlayerHandler.players[j] != null) {
				if (PlayerHandler.players[j].playerName.equalsIgnoreCase(playerName)) {
					disconnected = true;
					return true;
				}
			}
		}
		return false;
	}

	public void putInCombat(int attacker) {
		underAttackBy = attacker;
		logoutDelay.reset();
		singleCombatDelay.reset();
	}

	public int appendDamage(int damage, Hitmark h) {
		if (damage <= 0) {
			damage = 0;
			h = Hitmark.MISS;
		}
		if (playerLevel[playerHitpoints] - damage < 0) {
			damage = playerLevel[playerHitpoints];
		}
		if (teleTimer <= 0) {
			playerLevel[3] -= damage;
			if (!hitUpdateRequired) {
				hitUpdateRequired = true;
				hitDiff = damage;
				hitmark = h;
			} else if (!hitUpdateRequired2) {
				hitUpdateRequired2 = true;
				hitDiff2 = damage;
				secondHitmark = h;
			}
			this.getPA().refreshSkill(3);
		} else {
			if (hitUpdateRequired) {
				hitUpdateRequired = false;
			}
			if (hitUpdateRequired2) {
				hitUpdateRequired2 = false;
			}
		}
		updateRequired = true;
		return damage;
	}

	public void yell(String s) {
		for (int i = 0; i < Config.MAX_PLAYERS; i++) {
			if (validClient(i)) {
				getClient(i).sendMessage(s);
			}
		}
	}

	public void sendMessage(String s) {
		if (getOutStream() != null) {
			outStream.createFrameVarSize(253);
			outStream.writeString(s);
			outStream.endFrameVarSize();
		}
	}

	public int[] damageTaken = new int[Config.MAX_PLAYERS];

	public void handleHitMask(int damage) {
		if (!hitUpdateRequired) {
			hitUpdateRequired = true;
			hitDiff = damage;
		} else if (!hitUpdateRequired2) {
			hitUpdateRequired2 = true;
			hitDiff2 = damage;
		}
		updateRequired = true;
	}

	public Stream getOutStream() {
		return outStream;
	}

	public Stream outStream = null;
	Client c;

	public Allotments getAllotment() {
		return c.allotment;
	}

	public Bushes getBushes() {
		return c.bushes;
	}

	public PlayerAction getPlayerAction() {
		return playerAction;
	}

	public PlayerAssistant getPA() {
		// TODO Auto-generated method stub
		return c.getPA();
	}

	public ItemAssistant getItems() {
		// TODO Auto-generated method stub
		return c.getItems();
	}

	public AgilityHandler getAgilityHandler() {
		return agilityHandler;
	}
	public boolean isInBarrows() {
		if (absX > 3543 && absX < 3584 && absY > 3265 && absY < 3311) {
			return true;
		}
		return false;
	}

	public boolean isInBarrows2() {
		if (absX > 3529 && absX < 3581 && absY > 9673 && absY < 9722) {
			return true;
		}
		return false;
	}

	public long setBestZulrahTime(long bestZulrahTime) {
		return this.bestZulrahTime = bestZulrahTime;
	}

	public long getBestZulrahTime() {
		return bestZulrahTime;
	}

	public boolean inGodmode() {
		return godmode;
	}

	public void setGodmode(boolean godmode) {
		this.godmode = godmode;
	}

	private boolean godmode;
	private boolean safemode;

	public boolean inSafemode() {
		return safemode;
	}

	public void setSafemode(boolean safemode) {
		this.safemode = safemode;
	}

	/**
	 * Retrieves the rights for this player.
	 * 
	 * @return the rights
	 */
	public Rights getRights() {
		return rights;
	}

	/**
	 * Updates the rights for this player by comparing the players current rights to
	 * that of the available rights and assigning the first rank found.
	 */
	public void setRights(Rights rights) {
		this.rights = rights;
	}
	public boolean walkFromFilling = false;
	public boolean fillingWater;
	public int raidPoints;
	@Override
	public void reset() {
		// TODO Auto-generated method stub
		setChatTextUpdateRequired(false);
		setAppearanceUpdateRequired(false);
		forceMovementUpdateRequired = false;
	}

	public Bank getBank() {
		// TODO Auto-generated method stub
		return c.getBank();
	}

	public String getName() {
		// TODO Auto-generated method stub
		return playerName;
	}

	public Raids getRaids() {
		// TODO Auto-generated method stub
		return c.getRaids();
	}

	public int getIndex() {
		// TODO Auto-generated method stub
		return index;
	}
	public InterfaceClickHandler getRandomInterfaceClick() {
		return randomInterfaceClick;
	}
	public void setRaidPoints(int raidPoints) {
		this.raidPoints = raidPoints;
	}

	public int getRaidPoints() {
		return raidPoints;
	}
	public boolean inRaids() {
		return (absX > 3210 && absX < 3368 && absY > 5137 && absY < 5759);
	}

	public Slayer getSlayer() {
		if (c.slayer == null) {
			c.slayer = new Slayer(c);
		}
		return c.slayer;
	}

}