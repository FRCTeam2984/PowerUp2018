package org.ljrobotics.lib.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.io.File;

import org.json.simple.JSONObject;
import org.junit.Test;

public class ConstantsBaseTest {

	@Test
	public void constantEqualsItself() {
		ConstantsBase.Constant constant = new ConstantsBase.Constant("test", String.class, "test");
		assertEquals(constant, constant);
	}

	@Test
	public void constantDoesNotEqualNull() {
		ConstantsBase.Constant constant = new ConstantsBase.Constant("test", String.class, "test");
		assertNotEquals(constant, null);
	}

	@Test
	public void constantDoesNotEqualAString() {
		ConstantsBase.Constant constant = new ConstantsBase.Constant("test", String.class, "test");
		assertNotEquals(constant, "who");
	}

	@Test
	public void constantDoesNotEqualAConstantWithAnotherValue() {
		ConstantsBase.Constant constant = new ConstantsBase.Constant("test", String.class, "test");
		ConstantsBase.Constant constant2 = new ConstantsBase.Constant("test", String.class, "not");
		assertNotEquals(constant, constant2);
	}

	@Test
	public void getFileReturnsFileWithRegexReplaced() {
		ConstantsBase constants = new ConstantsBase() {

			@Override
			public String getFileLocation() {
				return "~";
			}

		};
		assertEquals(new File(System.getProperty("user.home")), constants.getFile());
	}

	static class Constants1 extends ConstantsBase {

		public static int test = 0;

		@SuppressWarnings("unchecked")
		@Override
		public JSONObject getJSONObjectFromFile() {
			JSONObject json = new JSONObject();
			json.put("test", 1L);
			return json;
		}

		@Override
		public String getFileLocation() {
			return "~";
		}
	}

	@Test
	public void loadFromFileSetsIntToNewValue() {
		Constants1 constants = new Constants1();
		constants.loadFromFile();
		assertEquals(1, Constants1.test);
	}

}
