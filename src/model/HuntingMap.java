/**
 * 
 */
package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.newdawn.slick.GameContainer;

import model.huntingMap.TerrainObject;

import core.ConstantStore;


/**
 * this manufactures the terrain for the hunt scene
 */
@SuppressWarnings("serial")
public class HuntingMap implements Serializable {
	/**width of the tile to be used for terrain*/
	public final double TILE_WIDTH = 48;
	/**HEIGHT of the tile to be used for terrain*/
	public final double TILE_HEIGHT = 48;
	/**density of terrain in this map - high value means very dense 1-100*/
	public final int MAP_DENSITY;
	/**how much rocks over forests dominate the landscape on this map 1 - 100*/
	public final int MAP_STONY;
	/**width of the hunting map	 */
	public final double MAP_WIDTH;
	/**height of the hunting map	 */
	public final double MAP_HEIGHT;
	/**x location of this section of the hunting map on the main map	 */
	public final double MAP_X_LOC;
	/**y location of this section of the hunting map on the main map */
	public final double MAP_Y_LOC;
	/** the number of images going across this map = map width/image width */
	public int mapXMax;
	/** the number of images going down this map = map height/image height */
	public int mapYMax;
	/**the background graphic type for this map*/
	private ConstantStore.bckGroundType bckGround;
	/** 2d array that holds each hunting grounds graphics object for the actual map*/
	private TerrainObject[][] huntingGroundsMap;
	/**random generator used for entire hunt map*/
	private Random huntMapRand;
	/**game context for making the terrain component*/
	private GameContainer context;

	
	/**
	 * builds the layout for the hunt scene
	 * @param dblMapStats an array of doubles holding the various numeric descriptive quantities of the map
	 * 		idx 0 = map width
	 * 		idx 1 = map height
	 * 		idx 2 = map main map x location
	 * 		idx 3 = map main map y location
	 * @param intMapStats integer array of stats of the map, related to how likely and what kind of terrain is to be painted
	 * 		idx 0 = how dense with terrain this section of the map is
	 * 		idx 1 = how much trees over rocks dominate this terrain
	 * @param mapDensity how dense with terrain this map is
	 * @param environs the environment of this map -determines the background gif
	 */
	public HuntingMap(GameContainer context, double[] dblMapStats, int[] intMapStats, ConstantStore.Environments environs) {
		this.MAP_WIDTH = dblMapStats[0];//in pixels
		this.MAP_HEIGHT = dblMapStats[1];//in pixels
		this.MAP_X_LOC = dblMapStats[2];
		this.MAP_Y_LOC = dblMapStats[3];
		
		this.context = context;
		
		this.MAP_DENSITY = intMapStats[0];
		this.MAP_STONY = intMapStats[1];
		
		this.mapXMax = (int)(this.MAP_WIDTH/this.TILE_WIDTH);
		this.mapYMax = (int)(this.MAP_HEIGHT/this.TILE_HEIGHT);
		
		switch (environs) {

		case FOREST :
		case PLAINS	: 		this.bckGround = ConstantStore.bckGroundType.GRASS;
							break;
		case HILLS :		
		case MOUNTAINS :	this.bckGround = ConstantStore.bckGroundType.MOUNTAIN;
							break;
			
		case DESERT :		this.bckGround = ConstantStore.bckGroundType.DESERT;
							break;
		
		case SNOWY_FOREST :
		case SNOWY_HILLS :
		case SNOWY_MOUNTAINS :
		case SNOWY_PLAINS :	this.bckGround = ConstantStore.bckGroundType.SNOW;
							break;
		default : this.bckGround = ConstantStore.bckGroundType.GRASS;
						break;
		
		}//end switch
		
		this.huntMapRand = new Random();
		huntingGroundsMap = new TerrainObject[this.mapXMax][this.mapYMax];
		this.generateMap();			
		//System.out.println(huntingGroundsMap.toString());
	}//constructor
	
	/**
	 * @return the bckGround
	 */
	public ConstantStore.bckGroundType getBckGround() {
		return bckGround;
	}

	/**
	 * generates the map for this hunting instance by building terrain layouts randomly.
	 */
	HuntTerrainGenerator mapGen;
	private void generateMap(){
		mapGen = new HuntTerrainGenerator(this.context, this.mapYMax, this.mapXMax, this.MAP_DENSITY, this.MAP_STONY, huntMapRand, 
																this.TILE_WIDTH, this.TILE_HEIGHT, this.bckGround);
		this.huntingGroundsMap = mapGen.getHuntingGroundsMap();
	}//generate map method


	/**
	 * @return the huntingGroundsMap
	 */
	public TerrainObject[][] getHuntingGroundsMap() {
		return huntingGroundsMap;
	}

	
	/**
	 * @return the tILE_WIDTH
	 */
	public double getTILE_WIDTH() {
		return TILE_WIDTH;
	}

	/**
	 * @return the tILE_HEIGHT
	 */
	public double getTILE_HEIGHT() {
		return TILE_HEIGHT;
	}

	/**
	 * @return the mAP_DENSITY
	 */
	public int getMAP_DENSITY() {
		return MAP_DENSITY;
	}

	/**
	 * @return the mAP_STONY
	 */
	public int getMAP_STONY() {
		return MAP_STONY;
	}

	/**
	 * @return the mAP_WIDTH
	 */
	public double getMAP_WIDTH() {
		return MAP_WIDTH;
	}

	/**
	 * @return the mAP_HEIGHT
	 */
	public double getMAP_HEIGHT() {
		return MAP_HEIGHT;
	}

	/**
	 * @return the mAP_X_LOC
	 */
	public double getMAP_X_LOC() {
		return MAP_X_LOC;
	}

	/**
	 * @return the mAP_Y_LOC
	 */
	public double getMAP_Y_LOC() {
		return MAP_Y_LOC;
	}


	/**
	 * george's wonder class 
	 * that generates the terrain for the hunt map
	 */
	private class HuntTerrainGenerator {
		//Rows and cols are final
		private final int totalRows;
		private final int totalCols;
		
		/**type to use when empty - can't be 0*/
		private final int TYPE_EMPTY = 1;
		/**width of tile used to paint map*/
		private final double TILE_WIDTH;
		/**height of tile used to paint map*/
		private final double TILE_HEIGHT;
		
		
		/** 2d array that holds each hunting grounds graphics object for the actual map*/
		private TerrainObject[][] huntingGroundsMap;
//		/**2d array of the components intended to hold the sprite for the actual map - to be used with addasgrid*/
//		private TerrainComponent[][] huntingGroundsComponents;
	
		/**holds random object from parent class - use 1 random object so can control generator by seeding if necessary*/
		Random huntMapRand;
		/**the background type that the map this generator populates has*/
		ConstantStore.bckGroundType bckGround;
		
		private Tiles[][] tiles;
		private int[][] types;
		
		/**
		 * builds the huntTerrainGenerator
		 * @param totalRows number of tiles in the y direction on the map
		 * @param totalCols number of tiles in the x direction on the map
		 * @param procChance chance that a particular tile will have terrain
		 * @param stoneProc chance that a particular terrain block will be stone(impassable), as opposed to trees(passable but difficult)
		 */
		public HuntTerrainGenerator(GameContainer context, int totalRows, int totalCols, int procChance, 
									int stoneProc, Random huntMapRand, double tileWidth, 
									double tileHeight, ConstantStore.bckGroundType bckGround) {
			this.totalRows = totalRows;
			this.totalCols = totalCols;
			this.huntMapRand = huntMapRand;
			this.TILE_WIDTH = tileWidth;
			this.TILE_HEIGHT = tileHeight;
			int currentType = 1;
			
			
			tiles = new Tiles[this.totalRows][this.totalCols];
			types = new int[this.totalRows][this.totalCols];
			huntingGroundsMap = new TerrainObject[this.totalRows][this.totalCols];
//			huntingGroundsComponents = new TerrainComponent[this.totalRows][this.totalCols];
			
			//Set up the border of empty tiles around our map
			for (int row = 0; row < this.totalRows; row++) {
				tiles[row][0] = Tiles.EMPTY;
				types[row][0] = 0;
				tiles[row][totalCols-1] = Tiles.EMPTY;
				types[row][totalCols-1] = TYPE_EMPTY;
			}
			for (int col = 0; col < this.totalCols; col++) {
				tiles[0][col] = Tiles.EMPTY;
				types[0][col] = 0;
				tiles[totalRows-1][col] = Tiles.EMPTY;
				types[totalRows-1][col] = TYPE_EMPTY;
			}
			for(int row = (totalRows-5)/2; row < (totalRows+5)/2; row++) {
				for(int col = (totalCols-4)/2; col < (totalCols+4)/2; col++) {
					tiles[row][col] = Tiles.EMPTY;
					types[row][col] = TYPE_EMPTY;
				}
			}
			//proc means we place an object
			//If we proc, place a tile and make it the right type.
			for (int row = 0; row < this.totalRows; row++) {
				for (int col = 0; col < this.totalCols; col++) {
					if (tiles[row][col] == null) {//check if this null tile gets some terrain
						if (this.huntMapRand.nextInt(100) < procChance) {//if so figure out what kind and place it
							currentType = (this.huntMapRand.nextInt(100) < stoneProc) ? 1 : 2;
							placeTile(row, col, huntMapRand, currentType, true);
						}
					}
				}
			}//for i rows
			
			//Make all the null tiles empty
			for(int row = 0; row < totalRows; row++) {
				for(int col = 0; col < totalCols; col++) {
					if (tiles[row][col] == null) {//if still no terrain, put default empty in place
						  tiles[row][col] = Tiles.EMPTY;
						  types[row][col] = TYPE_EMPTY;		//needs to be either 1 or 2 - originally had 0 in types, 
						  							//but i am building tileset with the empties with both tree1 and rock1 sets having an empty tile  						
					}//either gets terrain or it doesn't
					
					//build structure that holds terrain objects
					huntingGroundsMap[row][col] = buildTerrainObject(row, col);
//					huntingGroundsComponents[row][col] = buildTerrainComponents(context, huntingGroundsMap[row][col]);

				}//for col = cols
			}//for row = rows
			
		}//huntTerrainGenerator constructor
		
		
		/**
		 * @return the huntingGroundsMap
		 */
		public TerrainObject[][] getHuntingGroundsMap() {
			return this.huntingGroundsMap;
		}
		
		
		/**
		 * build a terrain object, holding the relevant information about the terrain and its effect on hunt gameplay at a particular location
		 * @param row the row in the map that this terrain object represents
		 * @param col the col in the map that this terrain object represents
		 * @return the completed terrain object
		 */
		
		private TerrainObject buildTerrainObject(int row, int col){
			
			//speed modification for moving through this terrain
			double objMoveMod;
			//chance shot is blocked each turn in this terrain
			double objStopShot;
			//if type is 1 then rock, 2 then tree
				String namePrefix = (types[row][col] == 1) ? ("rock1") : ("tree1");
				String nameExt = getFileName(this.getSingleCharTile(row,col), namePrefix);
				if (this.tiles[row][col] == Tiles.EMPTY){//9 is always empty tile, regardless of stone or tree
					objMoveMod = 1;
					objStopShot = 0;
				}//nameExt not --
				else {//not empty - determine whether should be impassable or just difficult (stone or tree)
					if (namePrefix.equals("rock1")){//impassable
						objMoveMod = 0;
						objStopShot = 1;
					} else {//tree
						objMoveMod = .6;
						objStopShot = .5;
					}//if either rock or stone
				}//if based on name extension
				return new TerrainObject(nameExt, row * this.TILE_WIDTH, col * this.TILE_HEIGHT, this.bckGround, objMoveMod, objStopShot, (int) TILE_WIDTH, (int) TILE_HEIGHT);								
		}//method buildTerrainObject
		
		
		/**
		 * builds the tile name from the generated data
		 * @param prefix what tile, from 0 to f hex in chars
		 * @param type what type of tile (atm either stone1 or tree1)

		 * @return a string holding the filename for the particular tile in question
		 */
			private String getFileName(String prefix, String type){	
				String retVal = type + prefix + ((huntMapRand.nextBoolean()) ? "1" : "0") ;
				return retVal;
			}
				
		//Places a tile
		private void placeTile(int row, int col, Random random, int currentType, boolean isFirst) {
			if(tiles[row][col] == null) {
				List<Tiles> possibleList = new ArrayList<Tiles>();
				//At the beginning, the tile can be anything
				for(Tiles tile : Tiles.values())
					possibleList.add(tile);
				//System.out.println(possibleList);
				Tiles finder;
				List<Tiles> helper = new ArrayList<Tiles>();
				List<Tiles> pretendList = new ArrayList<Tiles>();
				//Then, for each neighbor, go through and remove anything that they cant be next to
				
				finder = tiles[row - 1][col];
				helper.clear();
				if(finder == null) {
					for(Tiles tile : Tiles.values())
						helper.add(tile);
				} else {
					helper = finder.getPossible(4);
				}
				//System.out.println(helper);
				pretendList.clear();
				for(Tiles tile : possibleList)
					pretendList.add(tile);
				
				for (Tiles tile : pretendList) {
					if (!helper.contains(tile)) {
						possibleList.remove(tile);
					}
				}
				//System.out.println(possibleList);
				finder = tiles[row][col - 1];
				helper.clear();
				if(finder == null) {
					for(Tiles tile : Tiles.values())
						helper.add(tile);
				} else {
					helper = finder.getPossible(2);
				}
				pretendList.clear();
				for(Tiles tile : possibleList)
					pretendList.add(tile);
				
				for (Tiles tile : pretendList) {
					if (!helper.contains(tile)) {
						possibleList.remove(tile);
					}
				}
				finder = tiles[row][col + 1];
				helper.clear();
				if(finder == null) {
					for(Tiles tile : Tiles.values())
						helper.add(tile);
				} else {
					helper = finder.getPossible(1);
				}
				
				pretendList.clear();
				for(Tiles tile : possibleList)
					pretendList.add(tile);
				
				for (Tiles tile : pretendList) {
					if (!helper.contains(tile)) {
						possibleList.remove(tile);
					}
				}
								
				finder = tiles[row + 1][col];
				helper.clear();
				if(finder == null) {
					for(Tiles tile : Tiles.values())
						helper.add(tile);
				} else {
					helper = finder.getPossible(3);
				}
				pretendList.clear();
				for(Tiles tile : possibleList)
					pretendList.add(tile);
				
				for (Tiles tile : pretendList) {
					if (!helper.contains(tile)) {
						possibleList.remove(tile);
					}
				}
				
				finder = tiles[row + 1][col + 1];
				helper.clear();
				if(finder == null) {
					for(Tiles tile : Tiles.values())
						helper.add(tile);
				} else {
					helper = finder.getPossible(5);
				}
				pretendList.clear();
				for(Tiles tile : possibleList)
					pretendList.add(tile);
				
				for (Tiles tile : pretendList) {
					if (!helper.contains(tile)) {
						possibleList.remove(tile);
					}
				}
				
				finder = tiles[row - 1][col - 1];
				helper.clear();
				if(finder == null) {
					for(Tiles tile : Tiles.values())
						helper.add(tile);
				} else {
					helper = finder.getPossible(8);
				}
				pretendList.clear();
				for(Tiles tile : possibleList)
					pretendList.add(tile);
				
				for (Tiles tile : pretendList) {
					if (!helper.contains(tile)) {
						possibleList.remove(tile);
					}
				}
				
				finder = tiles[row - 1][col + 1];
				helper.clear();
				if(finder == null) {
					for(Tiles tile : Tiles.values())
						helper.add(tile);
				} else {
					helper = finder.getPossible(7);
				}
				pretendList.clear();
				for(Tiles tile : possibleList)
					pretendList.add(tile);
				
				for (Tiles tile : pretendList) {
					if (!helper.contains(tile)) {
						possibleList.remove(tile);
					}
				}
				
				finder = tiles[row + 1][col -1];
				helper.clear();
				if(finder == null) {
					for(Tiles tile : Tiles.values())
						helper.add(tile);
				} else {
					helper = finder.getPossible(6);
				}
				pretendList.clear();
				for(Tiles tile : possibleList)
					pretendList.add(tile);
				
				for (Tiles tile : pretendList) {
					if (!helper.contains(tile)) {
						possibleList.remove(tile);
					}
				}
				
				//Choose a random of the type possible, and make it the right type
				if(possibleList.size() != 0) {
					if(possibleList.contains(Tiles.EMPTY) && !isFirst) {
						tiles[row][col] = Tiles.EMPTY;
						types[row][col] = TYPE_EMPTY;
					} else {
						tiles[row][col] = possibleList.get(random.nextInt(possibleList.size()));
						types[row][col] = currentType;
					}
				}
				else {
					tiles[row][col] = Tiles.EMPTY;

					types[row][col] = TYPE_EMPTY;
					
					//System.out.println(row + " " + col);
				}
//				System.out.println(tiles[row][col] + "\n");
//				System.out.println(this.toString());
				
				//If we made an empty tile, we dont place anything nearby
				if(tiles[row][col] == Tiles.EMPTY) {
					types[row][col] = TYPE_EMPTY;
					return;
				}
				//System.out.println(possibleList);
				//System.out.println(tiles[row][col]);
				//Otherwise, place something in all neighbors
				placeTile(row - 1, col, random, currentType, false);
				placeTile(row, col + 1, random, currentType, false);
				placeTile(row, col - 1, random, currentType, false);
				placeTile(row + 1, col, random, currentType, false);
			}
		}

		public String toString() {
			StringBuilder str = new StringBuilder();
			
			for(int row = 0; row < this.totalRows; row++) {
				for(int col = 0; col < this.totalCols; col++) {
					if(tiles[row][col] != null) {
						//Char array for numbers, types array is fine
						str.append(tiles[row][col].getIndex() + " ");
						//str.append(drawMap(tiles[row][col].getIndex()) + " ");
						//str.append(types[row][col] == 1 ? "Stone" : types[row][col] == 2 ? "Trees" : "     ");
						//str.append(types[row][col]);
					} else {
						str.append(-1 + " ");
					}//if tiles ara at row,col is null, else
				}//for col 0 to totalcols
				str.append("\n");
			}//for row = 0 to totalrows
			
			return str.toString();
		}
		
		public int[][] getTypes() {
			return types;
		}
		
		/**
		 * returns the char equivalent of a single tile value
		 * @param row the row location in the tile array
		 * @param col the col location in the tile array
		 * @return the character sought
		 */
		public String getSingleCharTile(int row, int col){
			//Tiles tmp = this.tiles[row][col];
			//System.out.println("" + row +"|" +col + "| ord : " + tmp.toString());
			int index = this.tiles[row][col].getIndex();
			//System.out.println("" + row +"|" +col +"|" +index + "|" + tmp.toString());
			//converts index in array to be a character to be put in the2d char array of tiles
			String charTile = (index == 10) ? "a" : 
							(index == 11) ? "b" : 
							(index == 12) ? "c" : 
							(index == 13) ? "d" : 
							(index == 14) ? "e" : 
							(index == 15) ? "f" : Integer.toString(index);					
			return charTile;
		}
		
		/**
		 * takes 2d array of tile ints and converts to required chars for map implementation
		 * @return 2d array of generated tiles
		 */
		public String[][] getCharTiles() {
			String[][] charTiles = new String[this.totalRows][this.totalCols];
			for(int row = 0; row < this.totalRows; row++) {
				for(int col = 0; col < this.totalCols; col++) {
					charTiles[row][col] = getSingleCharTile(row,col);
				}//for col = 0 to tot
			}//for row = 0 to tot
			return charTiles;
		}// method getCharTiles
	}//class definition
	
	public String getHuntMap() {
		return mapGen.toString();
	}
	


	/**
	 * structure representing the tile being added to the map
	 */
	private enum Tiles {
		BOTRIGHT (3), 
		BOTFULL (2, 3), 
		BOTLEFT (2), 
		RIGHTFULL (1, 3), 
		FULL (0, 1, 2, 3), 
		LEFTFULL (0, 2), 
		TOPRIGHT (1), 
		TOPFULL (0, 1), 
		TOPLEFT (0), 
		EMPTY (),
		A (1, 2, 3), 
		B (0, 2, 3), 
		C (0, 1, 3), 
		D (0, 1, 2),
		E (1, 2),
		F (0, 3);
		
		private Set<Integer> filled = new HashSet<Integer>();
		
		private Tiles(int ... filled) {
			for(int x : filled) {
				this.filled.add(x);
			}
		}
		
		private Set<Integer> getFilled() {
			return filled;
		}
		
		//0 = empty, 1 = stone, 2 = tree
		/*
		 * switch on direction : 1 = left, 2 = right, 3 = up, 4 = down, 5 = diagUpLeft, 6 = diagUpRight, 7 = diagDownLeft, 8 = diagDownRight
		 * 
		 * determines next tile based on whether or not a particular corner of a tile has an overlapping graphic or not
		 * map out a tile  with upper left 0, upper right 1, lower left 2, lower right 3
		 * 
		 * if a tile has a graphic in 0, all adjacent tiles need graphic to match this
		 * 
		 * @param direction the direction a particular tile is looking to go
		 * 
		 */
		public List<Tiles> getPossible(int direction) {
			List<Tiles> returnList = new ArrayList<Tiles>();
			Set<Integer> matching = new HashSet<Integer>();
			Set<Integer> forbidden = new HashSet<Integer>();
			//Right
			switch (direction) {
			case 1:
				if (this.getFilled().contains(0)) {
					matching.add(1);
				} else {
					forbidden.add(1);
				}
				if (this.getFilled().contains(2)) {
					matching.add(3);
				} else {
					forbidden.add(3);
				}
				break;
			case 2:
				if (this.getFilled().contains(1)) {
					matching.add(0);
				} else {
					forbidden.add(0);
				}
				if (this.getFilled().contains(3)) {
					matching.add(2);
				} else {
					forbidden.add(2);
				}
				break;
			case 3:
				if (this.getFilled().contains(0)) {
					matching.add(2);
				} else {
					forbidden.add(2);
				}
				if (this.getFilled().contains(1)) {
					matching.add(3);
				} else {
					forbidden.add(3);
				}
				break;
			case 4:
				if (this.getFilled().contains(2)) {
					matching.add(0);
				} else {
					forbidden.add(0);
				}
				if (this.getFilled().contains(3)) {
					matching.add(1);
				} else {
					forbidden.add(1);
				}
				break;
			case 5:
				if (this.getFilled().contains(0)) {
					matching.add(3);
				} else {
					forbidden.add(3);
				}
				break;
			case 6:
				if (this.getFilled().contains(1)) {
					matching.add(2);
				} else {
					forbidden.add(2);
				}
				break;
			case 7:
				if (this.getFilled().contains(2)) {
					matching.add(1);
				} else {
					forbidden.add(1);
				}
				break;
			case 8:
				if (this.getFilled().contains(3)) {
					matching.add(0);
				} else {
					forbidden.add(0);
				}
				break;
			}
			
			
			for(Tiles tile : Tiles.values()) {
				boolean canUse = true;
				for(int needed : matching) {
					if(!tile.getFilled().contains(needed)) {
						canUse = false;
					}
				}
				for(int nonono : forbidden) {
					if(tile.getFilled().contains(nonono)) {
						canUse = false;
					}
				}
				if (canUse) {
					returnList.add(tile);
				}
			}
						
			return returnList;
		}
		
		public int getIndex() {
			return this.ordinal();
		}
	}//tiles enum
	
	/**
	 * debug function to draw map in ascii generated by functionality
	 * @param index particular map object to draw
	 * @return ascii rep of object
	 */
	private String drawMap(int index) {
		String str = "";
		switch (index) {
		case 0: 
			str = " _";
			break;
		case 1:
			str = "__";
			break;
		case 2:
			str = "_ ";
			break;
		case 3:
			str = " |";
			break;
		case 4:
			str = "  ";
			break;
		case 5:
			str = "| ";
			break;
		case 6:
			str = " -";
			break;
		case 7:
			str = "--";
			break;
		case 8:
			str = "- ";
			break;
		case 9:
			str = "  ";
			break;
		case 10:
			str = "_|";
			break;
		case 11:
			str = "|_";
			break;
		case 12:
			str = "-|";
			break;
		case 13:
			str = "|-";
			break;
		}
		
		
		return str;
	}//draw map method

}//class HuntingMap

