package scene.encounter;

import model.Notification;
import model.Party;

public class MessageEncounter extends Encounter {
	
	String[] messages;
	public MessageEncounter(Party party, int min, int max) {
		super(party, min, max);
		messages = new String[] 
				{ "You notice something staring at you from behind the trees.  It might be best to keep moving.",
				"Your party admires the forest for a bit before moving ahead.",
				"Your party sings \"Oh! Susanna\" to increase moral.",
				"You pause to think about your bright future in Oregon."};
	}
	
	@Override
	public EncounterNotification doEncounter() {
		return makeNotification();
	}

	@Override
	protected EncounterNotification makeNotification() {
		return new EncounterNotification(
				new Notification(messages[(int)(Math.random()*messages.length)]),
				null);
	}

}