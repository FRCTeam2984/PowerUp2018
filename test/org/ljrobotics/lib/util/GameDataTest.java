package org.ljrobotics.lib.util;

import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class GameDataTest {
	private GameData gd;
	
	private boolean paddleArraysSame(PaddleSide[] a, PaddleSide[] b) {
		if(a.length != b.length) {
			return false;
		}
		for(int i = 0; i < a.length; i++ ) {
			if(a[i] != b[i]) {
				return false;
			}
		}
		return true;
	}
	
	@Before
	public void before() {
		this.gd = null;
		try {
			gd = new GameData("LRR");
		}
		catch(IncorrectGameData e) {
			assertEquals(e,null);
		}
		assertNotEquals(gd,null);
	}
	
	@Test
	public void stringToPaddleSidesCorrect() {
		try {
			assertEquals(paddleArraysSame(gd.stringToPaddleSide("RRL"), new PaddleSide[] {PaddleSide.RIGHT, PaddleSide.RIGHT, PaddleSide.LEFT}), true);
	
		}
		catch(IncorrectGameData e) {
			assertEquals(e, null);
		}
	}
	
	@Test
	public void paddleSidesAreCorrect() {
		assertEquals(paddleArraysSame(gd.GetPaddleSides(), new PaddleSide[] {PaddleSide.LEFT, PaddleSide.RIGHT, PaddleSide.RIGHT} ), true);
	}
	
	@Test
	public void getPaddleSideCorrect() {
		assertEquals(gd.GetPaddleSide(0), PaddleSide.LEFT);
	}

}
