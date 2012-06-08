package fr.vitec.fmk.reflect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import fr.vitec.fmk.exception.VitecException;

/**
 * Classe utilitaire de gestion de la réflectivité.
 */
public class ReflectUtil {

	/** Préfixe des getters. */
	public static final String GETTER_PREFIX = "get";

	/** Préfixe des getters booléens. */
	public static final String BOOLEAN_GETTER_PREFIX = "is";

	/** Préfixe des setters. */
	public static final String SETTER_PREFIX = "set";

	/** Nombre 2 pour traitements des paramètres */
	private static final int NB_2_PARAMETERS = 2;

	/** Nombre 3 pour traitements des paramètres  */
	private static final int NB_3_PARAMETERS = 3;

	/** Nombre 4 pour traitements des paramètres  */
	private static final int NB_4_PARAMETERS = 4;

	/** Nombre 5 pour traitements des paramètres  */
	private static final int NB_5_PARAMETERS = 5;

	/**
	 * Constructeur par défaut
	 */
	private ReflectUtil() {
	}

	/**
	 * Retourne la méthode getter d'un attribut.
	 * @param obj Instance de l'objet.
	 * @param fieldName Attribut dont on veut le getter.
	 * @return La méthode getter de l'attribut.
	 */
	public static Method getGetterMethodByFieldName(Object obj, String fieldName) {
		StringBuffer suffix = new StringBuffer(fieldName.substring(0, 1).toUpperCase());
		// Prise en compte du cas particulier où le deuxième caractère est en majuscule.
		if (fieldName.length() > 1 && fieldName.substring(1, 2).equalsIgnoreCase(fieldName.substring(1, 2))) {
			suffix.append(fieldName.substring(1, 2).toLowerCase() + fieldName.substring(2));
		} else {
			suffix.append(fieldName.substring(1));
		}
		String getterName = "get" + suffix.toString();
		Method getterMethod = null;
		try {
			getterMethod = obj.getClass().getMethod(getterName);
		} catch (SecurityException e) {
			throw new VitecException(e);
		} catch (NoSuchMethodException e) {
			getterName = "is" + suffix.toString();
			try {
				getterMethod = obj.getClass().getMethod(getterName);
			} catch (SecurityException e2) {
				throwException(e2);
			} catch (NoSuchMethodException e2) {
				throwException(e2);
			}
		}
		return getterMethod;
	}
	
	/**
	 * Recherche tous les Getter d'un type donnée pour un objet
	 * @param object Objet à traiter
	 * @param classFilter Classe à filtrer
	 * @return Liste de tous les Getters de l'objet passé en paramètre
	 */
	public static List<Method> findAllGettersOfClass(Object object, Class<?> classFilter, boolean isMethod) {
		List<Method> getters = new ArrayList<Method>();
		Method[] methods = object.getClass().getMethods();
		for (Method method : methods) {
			if((method.getName().startsWith(GETTER_PREFIX) || 
					(isMethod && method.getName().startsWith(BOOLEAN_GETTER_PREFIX))) &&
					(classFilter==null ||classFilter.isAssignableFrom(method.getReturnType()))) {
				getters.add(method);
			}
		}
		return getters;
	}

	/**
	 * Recherche tous les Getter d'un type donnée pour un objet
	 * @param object Objet à traiter
	 * @return Liste de tous les Getters de l'objet passé en paramètre
	 */
	public static List<Method> findAllGettersOfClass(Object object) {
		return findAllGettersOfClass(object, null, false);
	}
	/**
	 * Lance une exception de type Service.
	 * @param e exception d'origine.
	 */
	private static void throwException(Exception e) {
		throw new VitecException(e);
	}

	/**
	 * Retourne la méthode setter d'un attribut.
	 * @param obj Instance de l'objet.
	 * @param fieldName Attribut dont on veut le setter.
	 * @param parameterType Type du paramètre du setter.
	 * @return La méthode setter de l'attribut.
	 */
	public static Method getSetterMethodByFieldName(Object obj, String fieldName, Class<?> parameterType) {
		String suffix = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
		String setterName = "set" + suffix;
		Method setterMethod = null;
		try {
			setterMethod = obj.getClass().getMethod(setterName, parameterType);
		} catch (SecurityException e) {
			throw new VitecException(e);
		} catch (NoSuchMethodException e) {
			throw new VitecException(e);
		}
		return setterMethod;
	}

	/**
	 * Invocation du getter d'un attribut.
	 * @param obj Instance de l'objet.
	 * @param fieldName Attribut dont on invoke le getter.
	 * @return La valeur de l'attribut.
	 */
	public static Object invokeGetterByFieldName(Object obj, String fieldName) {
		Object value = null;
		Method getterMethod = ReflectUtil.getGetterMethodByFieldName(obj, fieldName);
		try {
			value = getterMethod.invoke(obj);
		} catch (IllegalArgumentException e) {
			throw new VitecException(e);
		} catch (IllegalAccessException e) {
			throw new VitecException(e);
		} catch (InvocationTargetException e) {
			throw new VitecException(e);
		}
		return value;
	}

	/**
	 * Invocation du setter d'un attribut.
	 * @param obj Instance de l'objet.
	 * @param fieldName Attribut dont on invoke le setter.
	 * @param value Valeur à passer au setter.
	 * @param parameterType Type du paramètre du setter.
	 */
	public static void invokeSetterByFieldName(Object obj, String fieldName, Object value, Class<?> parameterType) {
		Method setterMethod = ReflectUtil.getSetterMethodByFieldName(obj, fieldName, parameterType);
		try {
			setterMethod.invoke(obj, value);
		} catch (IllegalArgumentException e) {
			throw new VitecException(e);
		} catch (IllegalAccessException e) {
			throw new VitecException(e);
		} catch (InvocationTargetException e) {
			throw new VitecException(e);
		}
	}

	/**
	 * Invocation dynamique de méthode avec un nombre variable de paramètres.
	 * @param objet Instance de l'objet.
	 * @param methodName Nom de la méthode.
	 * @param parameters Liste des paramètres.
	 * @throws IllegalAccessException Déclenché si accès interdit à la méthode.
	 * @throws InvocationTargetException Déclenché si problème d'invocation de la méthode.
	 */
	public static void callMethod(Object objet, String methodName, Object... parameters) throws IllegalAccessException, InvocationTargetException {
		for (Method method : objet.getClass().getDeclaredMethods()) {
			if (method.getName().equalsIgnoreCase(methodName)) {
				if (parameters != null && parameters.length > 0) {
					// Cas 1 seul paramètre
					if (parameters.length == 1) {
						method.invoke(objet, parameters[0]);
						break;
					}
					// Cas 2 paramètres
					if (parameters.length == NB_2_PARAMETERS) {
						method.invoke(objet, parameters[0], parameters[1]);
						break;
					}
					// Cas 3 paramètres
					if (parameters.length == NB_3_PARAMETERS) {
						method.invoke(objet, parameters[0], parameters[1], parameters[NB_2_PARAMETERS]);
						break;
					}
					// Cas 4 paramètres
					if (parameters.length == NB_4_PARAMETERS) {
						method.invoke(objet, parameters[0], parameters[1], parameters[NB_2_PARAMETERS], parameters[NB_3_PARAMETERS]);
						break;
					}
					// Cas 5 paramètres
					if (parameters.length == NB_5_PARAMETERS) {
						method.invoke(objet, parameters[0], parameters[1], parameters[NB_2_PARAMETERS], parameters[NB_3_PARAMETERS], parameters[NB_4_PARAMETERS]);
						break;
					}
					throw new InvocationTargetException(new IllegalArgumentException("Trop de paramètres"));
				} else {
					method.invoke(objet);
					break;
				}
			}
		}
	}

}
