package Ghreborn.model.players.skills.farming;

import Ghreborn.model.Location;

public class FarmingConstants {// todo farmers watching crops
	// payment to farmer
	// todo sack making with jute, sack filling with
	// items

public static final int WATERING_CAN_ANIM = 2293;
public static final int RAKING_ANIM = 2273;
public static final int SPADE_ANIM = 830;
public static final int SEED_DIBBING = 2291;
public static final int PICKING_VEGETABLE_ANIM = 2282;
public static final int PICKING_HERB_ANIM = 2279;
public static final int PUTTING_COMPOST = 2283;
public static final int CURING_ANIM = 2288;
public static final int FILLING_POT_ANIM = 2287;
public static final int PLANTING_POT_ANIM = 2272;
public static final int PRUNING_ANIM = 2275;

public static final int RAKE = 5341;
public static final int SEED_DIBBER = 5343;
public static final int SPADE = 952;
public static final int TROWEL = 5325;
public static final int SECATEURS = 5329;
public static final int MAGIC_SECATEURS = 7409;

public static final int[] WATERED_SAPPLING = {5364, 5365, 5366, 5367, 5368, 5369, 5488, 5489, 5490, 5491, 5492, 5493, 5494, 5495};

public static boolean inRangeArea(Location Location1, Location Location2, Location LocationChecked) {
int x = Location1.getX();
int y = Location1.getY();
int x1 = Location2.getX();
int y1 = Location2.getY();
return LocationChecked.getX() >= x && LocationChecked.getY() >= y && LocationChecked.getX() <= x1 && LocationChecked.getY() <= y1;
}
}
