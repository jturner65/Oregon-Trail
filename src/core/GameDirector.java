package core;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Color;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;
import org.newdawn.slick.state.transition.RotateTransition;
import org.newdawn.slick.state.transition.Transition;

import component.SegmentedControl;
import component.modal.ComponentModal;
import component.modal.Modal;
import core.ConstantStore.StateIdx;

import scene.*;
import scene.encounter.*;
import scene.test.*;

import model.*;
import model.worldMap.TrailEdge;

/**
 * Directs the logical functionality of the game. Sets everything in motion.
 */

public class GameDirector implements SceneListener {
	private final int SCREEN_WIDTH = 1024;
	private final int SCREEN_HEIGHT = 576;
	private final int FRAME_RATE = 60;
	
	private static GameDirector sharedDirector;
	
	private FontStore fontManager;
	
	private SceneDirector sceneDirector;
	private AppGameContainer container;
	
	private Game game;
	private WorldMap worldMap;
	/**
	 * Constructs a game director object.
	 */
	private GameDirector() {
		 sharedDirector = this;
		 
		 //fontManager = new FontManager();

		 sceneDirector = new SceneDirector("Oregon Trail");
		 worldMap = new WorldMap(120);
		 game = new Game(worldMap);
	}
	
	/**
	 * Singleton Design pattern. Returns the single instance of game director object that drives the game, for the scenes to interact with.
	 * @return handle of game director for scene to interact with
	 */
	public static SceneListener sharedSceneListener() {
		return sharedDirector;
	}

	/**
	 * Launches window that contains game.
	 */
	public void start() {
		try {
			container = new AppGameContainer(sceneDirector);
			container.setDisplayMode(SCREEN_WIDTH, SCREEN_HEIGHT, false);
			container.setTargetFrameRate(FRAME_RATE);
			container.setAlwaysRender(true);
			container.setShowFPS(false);
			container.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	

	/**
	 * Returns the container that holds the gui element of the game.
	 * @return the game container
	 */
	public AppGameContainer getContainer() {
		return container;
	}
	
	/**
	 * Determines the appropriate game scene to return based on a passed id.
	 * @param id the {@code SceneID} desired
	 * @return the handle to the newly created scene
	 */
	private Scene sceneForSceneID(SceneID id) {
		switch (id) {
		case SPLASH:
			return new SplashScene();
		case LOADING:
			return new LoadingScene();
		case MAINMENU:
			return new MainMenuScene();
		case PARTYCREATION:
			return new PartyCreationScene(game.getPlayer());
		case TOWN:
			game.resetStoreInventory(worldMap.getCurrLocationNode());
			return new TownScene(game.getPlayer().getParty(), worldMap.getCurrLocationNode());
		case STORE:
			return new StoreScene(game.getPlayer().getParty(), game.getStoreInventory(), (1 + worldMap.getCurrLocationNode().getRank()/10));
		case PARTYINVENTORY:
			return new PartyInventoryScene(game.getPlayer().getParty());
		case SCENESELECTOR:
			SoundStore.get().stop();
			return new SceneSelectorScene(game);
		case HUNT:
			return new HuntScene(game.getPlayer().getParty(), game.getWorldMap());
		case TRAIL:
			return new TrailScene(game.getPlayer().getParty(), new RandomEncounterTable(getEncounterList()));
		case GAMEOVER:
			return new GameOverScene();
		case VICTORY:
			return new VictoryScene();
		case COMPONENTTEST:
			return new ComponentTestScene();
		case TRAILTEST:
			return new TrailTestScene(game.getPlayer().getParty());
		case MAP:
			return new MapScene(game.getWorldMap());
		case RIVER:
			return new RiverScene(game.getPlayer().getParty());
		case OPTIONS:
			return new OptionsScene();
		case TAVERN:
			return new TavernScene(game.getPlayer().getParty());
		}
		
		return null;
	}

	/*----------------------
	  SceneDelegate
	  ----------------------*/
	@Override
	public void requestScene(SceneID id, Scene lastScene, boolean popLastScene) {
		Transition outTransition = null;
		Transition inTransition = null;
		
		if(id != SceneID.SPLASH && id != SceneID.LOADING) {
			SoundStore.get().stopSound("Steps");
		}
		
		if (game.getPlayer().getParty() != null && game.getPlayer().getParty().getLocation() != null) {
			worldMap.setCurrLocationNode(game.getPlayer().getParty().getLocation());
			worldMap.setCurrTrail(game.getPlayer().getParty().getTrail());
		}
		
		Scene newScene = id != null ? sceneForSceneID(id): null;
		
		if (worldMap.getCurrLocationNode().getRank() == worldMap.getMaxRank()) {
			newScene = new VictoryScene();
		}
		
		if (newScene instanceof VictoryScene || newScene instanceof GameOverScene) {
			inTransition = new RotateTransition(Color.black);
		} else if (newScene instanceof PartyInventoryScene && lastScene instanceof StoreScene) {
			// Requested Party Inventory Scene
			newScene = new PartyInventoryScene(game.getPlayer().getParty(), ((StoreScene) lastScene).getInventory());
		} else if (lastScene instanceof PartyCreationScene) {
			// Last scene was Party Creation Scene
			game.getPlayer().getParty().setLocation(game.getWorldMap().getMapHead());
		} else if (newScene instanceof PartyInventoryScene && lastScene instanceof HuntScene ) {
			//Going to PIS from Hunt - we only want to show the hunter's inventory
			Party currentParty = game.getPlayer().getParty();
			List<Person> temporary = new ArrayList<Person>();
			temporary.add(game.getPlayer().getParty().getPartyMembers().get(0));
			Party hunterOnly = new Party(currentParty.getPace(), currentParty.getRations(), temporary.get(0), temporary, currentParty.getTime());
			newScene = new PartyInventoryScene(hunterOnly);
		}
		
		if (newScene != null) {
			lastScene.disable();
			
			if (outTransition == null) {
				outTransition = new FadeOutTransition(Color.black);
			}
			if (inTransition == null) {
				inTransition = new FadeInTransition(Color.black);
			}
			
			sceneDirector.pushScene(newScene, popLastScene, true, outTransition, inTransition);
		}
	}
	
	@Override
	public void sceneDidEnd(Scene scene) {
		scene.disable();
		sceneDirector.popScene(true);
	}
	
	@Override
	public FontStore getFontManager() {
		return fontManager;
	}
	
	@Override
	public void showSceneSelector() {
		sceneDirector.replaceStackWithScene(sceneForSceneID(SceneID.SCENESELECTOR));
	}
	
	public void resetToMainMenu() {
		sceneDirector.replaceStackWithScene(sceneForSceneID(SceneID.MAINMENU));
		game = new Game(game.getWorldMap());
	}
	
	/**
	 * Gets the encounter list to populate the trail scenes random encounter table
	 * @return The mapping of scene types to probabilities
	 */
	private List<Encounter> getEncounterList() {
		Random rand = new Random();
		List<Encounter> encounterList = new ArrayList<Encounter>();
		
		for ( EncounterID encounter : EncounterID.values() ) {
			encounterList.add(EncounterID.getEncounter(game.getPlayer().getParty(), encounter, 5 * (rand.nextInt(10)+1)));
		}
		return encounterList;
	}
	
	@Override
	public void serialize(String saveName) {
		try {
			File newFile = new File(ConstantStore.PATH_SERIAL + saveName + ".ser");
			if(!newFile.exists()) {
				new File(ConstantStore.PATH_SERIAL).mkdir();
				newFile.createNewFile();
			}
			FileOutputStream fileOut = new FileOutputStream(newFile);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(game);
			out.close();
			fileOut.close();
		} catch (IOException i) {
			i.printStackTrace();
		}
	}
	
	@Override
	public Game deserialize(String saveName) {
		try {
			FileInputStream fileIn = new FileInputStream(ConstantStore.PATH_SERIAL + saveName + ".ser");
			ObjectInputStream in = new ObjectInputStream(fileIn);
			game = (Game) in.readObject();
			in.close();
			fileIn.close();
		} catch (IOException i) {
			i.printStackTrace();
			return null;
		} catch (ClassNotFoundException e) {
			Logger.log("Game class not found", Logger.Level.INFO);
			e.printStackTrace();
			return null;
		}
	
		if(game.getPlayer().getParty() == null) {
			sceneDirector.pushScene(new PartyCreationScene(game.getPlayer()), true, true, new FadeOutTransition(Color.black), new FadeInTransition(Color.black));	
		} else if (game.getPlayer().getParty().getTrail() == null || 
				game.getPlayer().getParty().getTrail().getConditionPercentage() == 0) {
			sceneDirector.pushScene(new TownScene(game.getPlayer().getParty(), game.getPlayer().getParty().getLocation()), 
					true, true, new FadeOutTransition(Color.black), new FadeInTransition(Color.black));	
		} else {
			sceneDirector.pushScene(new TrailScene(game.getPlayer().getParty(), new RandomEncounterTable(getEncounterList())), true, true, new FadeOutTransition(Color.black), new FadeInTransition(Color.black));
		}
		return game;
	}

	/*----------------------
	  Saving and Loading
	  ----------------------*/
	/**
	 * 
	 */
	public Modal getLoadSaveModal(boolean isLoad, Scene scene) {

		List<String> fileList = new ArrayList<String>();
		File file  = new File(ConstantStore.PATH_SERIAL);
		if(file.exists()) {
			for(String fileName : file.list()) {
				fileList.add(fileName);
			}
		}
		
		String[] saveFileList = new String[5];
		int numEmpty = 0;
		for(int i = 1; i <= 5; i++) {
			if(fileList.contains("Game " + i + ".ser")) {
				saveFileList[i-1] = ("Game " + i);
			} else {
				saveFileList[i-1] = ("Empty");
				numEmpty++;
			}
		}
		int[] emptyFiles = new int[numEmpty];
		int j = 0;
		for(int i = 0; i < 5; i++) {
			if(saveFileList[i] == "Empty") {
				emptyFiles[j] = i;
				j++;
			}
		}
		
		SegmentedControl loadableFiles = new SegmentedControl(container, 500, 200, 5, 1, 0, true, 1, saveFileList);
		loadableFiles.setDisabled(emptyFiles);
		
		if (isLoad) {
			Modal fileLoadModal = new ComponentModal<SegmentedControl>(container, scene, "Choose a file to load", 2, loadableFiles);
			fileLoadModal.setButtonText(fileLoadModal.getCancelButtonIndex(), ConstantStore.get("GENERAL", "CANCEL"));
			return fileLoadModal;
		} else {
			Modal fileSaveModal = new ComponentModal<SegmentedControl>(container, scene, "Choose a slot to save", 2, new SegmentedControl(container, 500, 200, 5, 1, 0, true, 1, saveFileList));
			fileSaveModal.setButtonText(fileSaveModal.getCancelButtonIndex(), ConstantStore.get("GENERAL", "CANCEL"));
			return fileSaveModal;
		}
	}
	

	@Override
	public void updateMap(StateIdx state) {
		worldMap.setVisibleByArea(state);		
	}
	
	/*----------------------
	  Main
	  ----------------------*/
	/**
	 * The game main method
	 * @param args
	 */
	public static void main(String[] args) {
		Logger.log("-----------------NEW GAME STARTED-----------------", Logger.Level.INFO);
		GameDirector gameDirector = new GameDirector();
		gameDirector.start();
	}

	@Override
	public TrailEdge trailBlaze() {
		if (worldMap.getNextRankOrphanLocationList().size() != 0) {
			return worldMap.makePCInboundTrail(worldMap.getNextRankOrphanLocation());
		}
		return null;
	}
}
