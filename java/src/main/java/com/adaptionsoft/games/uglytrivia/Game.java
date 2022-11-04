package com.adaptionsoft.games.uglytrivia;

import java.util.ArrayList;
import java.util.LinkedList;

// General comment :
// Better to use this keyword when calling inner variables, methods, ...
// and to disambiguate references
public class Game {
	// It would be better to specify the type of list
	// for better comprehension
    ArrayList players = new ArrayList();
    int[] places = new int[6];
    int[] purses  = new int[6];
    boolean[] inPenaltyBox  = new boolean[6];
    //Same here, specify type
    LinkedList popQuestions = new LinkedList();
    LinkedList scienceQuestions = new LinkedList();
    LinkedList sportsQuestions = new LinkedList();
    LinkedList rockQuestions = new LinkedList();
    
    int currentPlayer = 0;
    boolean isGettingOutOfPenaltyBox;
    
    public  Game(){
    	for (int i = 0; i < 50; i++) {
			popQuestions.addLast("Pop Question " + i);
			// could pass simple string as the 1st object without parenthesis
			scienceQuestions.addLast(("Science Question " + i));
			// could pass simple string as the 1st object without parenthesis
			sportsQuestions.addLast(("Sports Question " + i));
			// We can also do a simple refactoring here to use this method
			// to return the param to add for all this lists
			rockQuestions.addLast(createRockQuestion(i));
    	}
    }

	/**
	 * Refactored method for question creation
	 *
	 * @param question question string
	 * @param index question number
	 * @return a combined string of question and number
	 */
	private String createQuestion(String question, int index) {
		return question + " " + index;
	}

	// better use the refactored method above
	public String createRockQuestion(int index){
		return "Rock Question " + index;
	}

	// Unused method, better to delete it
	public boolean isPlayable() {
		return (howManyPlayers() >= 2);
	}

	// the return type (boolean) of this method is unused in main test
	// i suggest to change it to void
	public boolean add(String playerName) {
		// Unnecessary empty lines here
		
	    players.add(playerName);
		// it's going to raise an ArrayIndexOutOfBoundsException if number of players is > 6
		// whether to increase the array size to max players number or to add a try catch block
	    places[howManyPlayers()] = 0;
		// it's going to raise an ArrayIndexOutOfBoundsException if number of players is > 6
		// whether to increase the array size to max players number or to add a try catch block
	    purses[howManyPlayers()] = 0;
		// it's going to raise an ArrayIndexOutOfBoundsException if number of players is > 6
		// whether to increase the array size to max players number or to add a try catch block
	    inPenaltyBox[howManyPlayers()] = false;
	    
	    System.out.println(playerName + " was added");
	    System.out.println("They are player number " + players.size());
		return true;
	}

	// it's not necessary to make this method as public,
	// it's only used within this class
	// i suggest to make it private or protected
	public int howManyPlayers() {
		return players.size();
	}

	public void roll(int roll) {
		System.out.println(players.get(currentPlayer) + " is the current player");
		System.out.println("They have rolled a " + roll);
		
		if (inPenaltyBox[currentPlayer]) {
			if (roll % 2 != 0) {
				isGettingOutOfPenaltyBox = true;
				
				System.out.println(players.get(currentPlayer) + " is getting out of the penalty box");
				movePlayerAndAskQuestion(roll);
			} else {
				System.out.println(players.get(currentPlayer) + " is not getting out of the penalty box");
				isGettingOutOfPenaltyBox = false;
				}
			
		} else {

			movePlayerAndAskQuestion(roll);
		}
		
	}

	private void movePlayerAndAskQuestion(int roll) {
		// can be simplified to
		// places[currentPlayer] += roll;
		places[currentPlayer] = places[currentPlayer] + roll;
		// same here places[currentPlayer] -= 12
		// 11 and 12 can be declared as static values in class level
		if (places[currentPlayer] > 11) places[currentPlayer] = places[currentPlayer] - 12;

		System.out.println(players.get(currentPlayer)
                + "'s new location is "
                + places[currentPlayer]);
		System.out.println("The category is " + currentCategory());
		askQuestion();
	}

	private void askQuestion() {
		// == check can be replaced by equals
		// better to use here also static variables for question category
		// to prevent duplication and errors when manually writing them
		if (currentCategory() == "Pop")
			System.out.println(popQuestions.removeFirst());
		if (currentCategory() == "Science")
			System.out.println(scienceQuestions.removeFirst());
		if (currentCategory() == "Sports")
			System.out.println(sportsQuestions.removeFirst());
		if (currentCategory() == "Rock")
			System.out.println(rockQuestions.removeFirst());		
	}
	
	
	private String currentCategory() {
		// can be simplified in a one condition
		// if (places[currentPlayer] == 0 || places[currentPlayer] == 4 || places[currentPlayer] == 8) return "Pop";
		// same goes for other conditions
		// Same problem here if current player is > 6, arrays exception
		if (places[currentPlayer] == 0) return "Pop";
		if (places[currentPlayer] == 4) return "Pop";
		if (places[currentPlayer] == 8) return "Pop";
		if (places[currentPlayer] == 1) return "Science";
		if (places[currentPlayer] == 5) return "Science";
		if (places[currentPlayer] == 9) return "Science";
		if (places[currentPlayer] == 2) return "Sports";
		if (places[currentPlayer] == 6) return "Sports";
		if (places[currentPlayer] == 10) return "Sports";
		return "Rock";
	}

	public boolean wasCorrectlyAnswered() {
		if (inPenaltyBox[currentPlayer]){
			if (isGettingOutOfPenaltyBox) {
				System.out.println("Answer was correct!!!!");
				currentPlayer++;
				if (currentPlayer == players.size()) currentPlayer = 0;
				purses[currentPlayer]++;
				System.out.println(players.get(currentPlayer)
						+ " now has "
						+ purses[currentPlayer]
						+ " Gold Coins.");

				// could inline this code
				// and do directly return didPlayerWin();
				boolean winner = didPlayerWin();

				return winner;
			} else {
				currentPlayer++;
				if (currentPlayer == players.size()) currentPlayer = 0;
				return true;
			}
			
			
			
		} else {
		
			System.out.println("Answer was corrent!!!!");
			purses[currentPlayer]++;
			System.out.println(players.get(currentPlayer) 
					+ " now has "
					+ purses[currentPlayer]
					+ " Gold Coins.");
			
			boolean winner = didPlayerWin();
			currentPlayer++;
			if (currentPlayer == players.size()) currentPlayer = 0;
			
			return winner;
		}
	}
	
	public boolean wrongAnswer(){
		System.out.println("Question was incorrectly answered");
		System.out.println(players.get(currentPlayer)+ " was sent to the penalty box");
		inPenaltyBox[currentPlayer] = true;
		
		currentPlayer++;
		if (currentPlayer == players.size()) currentPlayer = 0;
		return true;
	}


	private boolean didPlayerWin() {
		return !(purses[currentPlayer] == 6);
	}
}
