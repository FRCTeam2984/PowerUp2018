package edu.wpi.first.networktables;

import java.lang.reflect.Field;

public class NetworkTablesJNIWorkaround {

	public static void applyWorkaround() {
		try {
//			Field libraryLoaded = NetworkTablesJNI.class.getDeclaredField("libraryLoaded");
//			setStatic(libraryLoaded, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * from https://stackoverflow.com/questions/3301635/change-private-static-final-field-using-java-reflection
	 * @param field
	 * @param newValue
	 * @throws Exception
	 */
	private static void setStatic(Field field, Object newValue) throws Exception {
	      field.setAccessible(true);

	      field.set(null, newValue);
	   }
	
}
