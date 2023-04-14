package com.github.glfrazier.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class SystemProperties {

	public static enum NoPropertyBehavior {
		/** Return <code>null</code> when unset properties are gotten. */
		RETURN_NULL,
		/** Throw an IllegalArgumentException when unset properties are gotten. */
		THROW_EXCEPTION,
		/**
		 * Print a stack trace to stderr and exit with an error code of -1 when unset
		 * properties are gotten.
		 */
		TERMINATE
	};

	private static NoPropertyBehavior noPropertyBehavior = NoPropertyBehavior.THROW_EXCEPTION;

	static {
		if (System.getProperty("systemproperties.behavior") != null) {
			try {
				noPropertyBehavior = NoPropertyBehavior.valueOf(System.getProperty("systemproperties.behavior"));
			} catch (Throwable t) {
				throw new IllegalArgumentException("Illegal value for the property 'systemproperties.behavior': '"
						+ System.getProperty("systemproperties.behavior") + "'\n"
						+ "  Acceptable values are: null, 'RETURN_NULL', 'THROW_EXCEPTION' or 'TERMINATE.'");
			}
		}
	}

	/**
	 * Defines how SystemProperties responds when an unset property is gotten and no
	 * default value is specified. It is specified via the system property
	 * "systemproperties.behavior", which must be set using the '-D' flag. Example:
	 * 
	 * <pre>
	 * java -Dsystemproperties.behavior=RETURN_NULL ...
	 * </pre>
	 * 
	 * The available behaviors are specified in {@link #NoPropertyBehavior}. The
	 * default behavior is THROW_EXCEPTION (an IllegalArgumentException is thrown).
	 * 
	 * @see #NoPropertyBehavior
	 */
	public static final NoPropertyBehavior NO_PROPERTY_BEHAVIOR = noPropertyBehavior;

	public static Properties processArguments(String[] args) {
		Properties result = new Properties();
		result.putAll(System.getProperties());
		for (int i = 0; i < args.length; i++) {
			String[] varValPair = args[i].split("=");
			if (varValPair.length != 2) {
				if (args[i].charAt(args[i].length() - 1) == '=') {
					String vName = varValPair[0];
					varValPair = new String[2];
					varValPair[0] = vName;
					varValPair[1] = "";
				} else {
					System.err.println("All arguments must be of the form <name>=<value>.");
					System.err.println("Argument " + i + ": " + args[i]);
					System.exit(-1);
				}
			}
			// Need code here for the parameters file
			String var = varValPair[0];
			String val = varValPair[1];
			result.setProperty(var, val);
			while (result.containsKey("propertiesfile")) {
				FileInputStream inStream;
				String fileName = result.remove("propertiesfile").toString();
				System.out.println("Loading property file <" + fileName + ">.");
				try {
					inStream = new FileInputStream(fileName);
					Properties p = new Properties();
					p.load(inStream);
					inStream.close();
					result.putAll(p);
				} catch (IOException e) {
					new Exception("Failed to load the properties file <" + fileName + ">: " + e).printStackTrace();
					System.exit(-1);
				}
			}
		}
		return result;
	}

	public static Number getNumericProperty(String propName, Class<? extends Number> type) {
		if (System.getProperty(propName) == null) {
			return nullPropertyResponse(propName);
		}
		if (type.equals(Float.class)) {
			return Float.parseFloat(System.getProperty(propName));
		}
		if (type.equals(Double.class)) {
			return Double.parseDouble(System.getProperty(propName));
		}
		if (type.equals(Integer.class)) {
			return Integer.parseInt(System.getProperty(propName));
		}
		if (type.equals(Long.class)) {
			return Long.parseLong(System.getProperty(propName));
		}
		throw new IllegalArgumentException(
				"getNumericProperty only understand Double, Float, Long and Integer, not " + type);
	}

	/**
	 * The only difference between this method and System.getProperty(propName) is
	 * the behavior if the desired property is not set.
	 * 
	 * @param propName the name of the property
	 * @return the value of the property
	 * @see #NO_PROPERTY_BEHAVIOR
	 */
	public static String getStringProperty(String propName) {
		if (System.getProperty(propName) == null) {
			nullPropertyResponse(propName);
			return null;
		}
		return System.getProperty(propName);
	}

	public static float getFloatProperty(String propName) {
		return getNumericProperty(propName, Float.class).floatValue();
	}

	public static int getIntProperty(String propName) {
		return getNumericProperty(propName, Integer.class).intValue();
	}

	public static double getDoubleProperty(String propName) {
		return getNumericProperty(propName, Double.class).doubleValue();
	}

	private static Number nullPropertyResponse(String propName) {
		switch (NO_PROPERTY_BEHAVIOR) {
		case RETURN_NULL:
			return null;
		case THROW_EXCEPTION:
			throw new IllegalArgumentException("Property <" + propName + "> is not set.");
		case TERMINATE:
			new IllegalArgumentException("Property <" + propName + "> is not set.").printStackTrace();
			System.exit(-1);
		}
		return null;
	}

	public static Number getNumericProperty(String propName, float defaultValue) {
		if (System.getProperty(propName) == null) {
			return defaultValue;
		}
		return Float.parseFloat(System.getProperty(propName));
	}

	public static Number getNumericProperty(String propName, double defaultValue) {
		if (System.getProperty(propName) == null) {
			return defaultValue;
		}
		return Double.parseDouble(System.getProperty(propName));
	}

	public static Number getNumericProperty(String propName, int defaultValue) {
		if (System.getProperty(propName) == null) {
			return defaultValue;
		}
		return Integer.parseInt(System.getProperty(propName));
	}

	public static Number getNumericProperty(String propName, long defaultValue) {
		if (System.getProperty(propName) == null) {
			return defaultValue;
		}
		return Long.parseLong(System.getProperty(propName));
	}

	public static float getFloatProperty(String propName, float defaultValue) {
		return getNumericProperty(propName, defaultValue).floatValue();
	}

	public static int getIntProperty(String propName, int defaultValue) {
		return getNumericProperty(propName, defaultValue).intValue();
	}

	public static double getDoubleProperty(String propName, double defaultValue) {
		return getNumericProperty(propName, defaultValue).doubleValue();
	}

}
