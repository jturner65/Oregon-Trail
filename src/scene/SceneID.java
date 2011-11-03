package scene;

public enum SceneID {
	DEFAULT ("Default"),
	MAINMENU ("Main Menu"),
	PARTYCREATION ("Party Creation"),
	STORE ("Store"),
	PARTYINVENTORY ("Party Inventory"),
	TOWN ("Town"),
	HUNT ("Hunt"),
	TRAIL ("Trail"),
	GAMEOVER ("Game Over"),
	VICTORY ("Victory"),
	SCENESELECTOR ("Scene Selector"),
	COMPONENTTEST ("Component Test"),
	TRAILTEST ("Trail Test"),
	MAP ("Map"),
	CAMP ("Camp"),
	RIVER ("River");
	
	private final String name;
	
	private SceneID(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public String toString() {
		return getName();
	}
}
