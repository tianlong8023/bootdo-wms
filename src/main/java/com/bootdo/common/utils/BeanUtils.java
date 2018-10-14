package com.bootdo.common.utils;

import com.bootdo.common.annotation.DateTimeFormat;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.text.ParseException;
import java.util.*;

public final class BeanUtils {
	public static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];
	public static final String classPropertieName = "class";

	private BeanUtils() {
	}

	/**
	 * 拷贝属性，支持复杂对象、数组、集合及嵌套 添加Date,String互转。要应用这个特性须在Date类型field上配置注解<br>
	 * {@link com.bootdo.common.annotation.DateTimeFormat DateTimeFormat}
	 * (pattern = XXX如：Constants.DEFULT_DATE_FORMATTER)
	 * 
	 * @param dest
	 *            目标对象
	 * 
	 * @param orig
	 *            源对象
	 */
	public static void copyProperties(Object dest, Object orig) {
		if (dest == null || orig == null)
			return;
		PropertyDescriptor[] propertiesD = getPropertyDescriptors(dest.getClass());
		PropertyDescriptor[] propertiesO = getPropertyDescriptors(orig.getClass());
		Map<String, PropertyDescriptor> m = new HashMap<String, PropertyDescriptor>();
		if (CommonUtil.isEmpty(propertiesD) || CommonUtil.isEmpty(propertiesO))
			return;
		for (PropertyDescriptor po : propertiesO) {
			if (isClass(po))
				continue;
			m.put(po.getName(), po);
		}
		for (PropertyDescriptor pd : propertiesD) {
			if (isClass(pd))
				continue;
			PropertyDescriptor po = m.get(pd.getName());
			if (CommonUtil.isEmpty(po))
				continue;
			if (simpleCopy(po, pd)) {
				copyPropertie(dest, orig, pd, po);
			} else {
				customCopyPropertie(dest, orig, pd, po);
			}
		}
	}

	/**
	 * 拷贝属性，支持复杂对象、数组、集合及嵌套<br>
	 * 添加Date,String互转。要应用这个特性须在Date类型field上配置注解<br>
	 * {@link com.bootdo.common.annotation.DateTimeFormat DateTimeFormat}
	 * (pattern = XXX如：Constants.DEFULT_DATE_FORMATTER)
	 * 
	 * @param c
	 *            目标对象类型
	 * @param orig
	 *            源对象
	 * 
	 * @return T.newInstance()&copyFrom orig
	 */
	public static <T> T copyProperties(Class<T> c, Object orig) {
		T dest = null;
		if (CommonUtil.isEmpty(orig) || CommonUtil.isEmpty(c)) {
			return dest;
		}
		try {
			dest = c.newInstance();
		} catch (InstantiationException e) {
			// e.printStackTrace();
		} catch (IllegalAccessException e) {
			// e.printStackTrace();
		}
		if (null == dest)
			return dest;
		copyProperties(dest, orig);
		return dest;
	}

	@SuppressWarnings("unchecked")
	public static <T> T[] copyArray(Class<T> c, Object orig) {
		if (CommonUtil.isEmpty(c) || null == orig || !isArray(orig.getClass())) {
			try {
				Object newInstance = Array.newInstance(c, 0);
				return (T[]) newInstance;
			} catch (NegativeArraySizeException e) {
				// e.printStackTrace();
			}
		}
		return (T[]) copyArray(c, orig, null);
	}

	@SuppressWarnings({ "unchecked" })
	public static <T> List<T> copyList(Class<T> c, Object orig) {
		if (c == null || CommonUtil.isEmpty(orig) || !isList(orig.getClass()))
			return new ArrayList<T>();
		return copyList(c, orig, null);
	}

	@SuppressWarnings({ "unchecked" })
	public static <T> Set<T> copySet(Class<T> c, Object orig) {
		if (c == null || CommonUtil.isEmpty(orig) || !isSet(orig.getClass()))
			return new HashSet<T>();
		return copySet(c, orig, null);
	}

	/**
	 * 拷贝map到map
	 * 
	 * @param c
	 * @param map
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <K, T> Map<K, T> copyMap(Class<T> c, Map map) {
		if (c == null || CommonUtil.isEmpty(map))
			return null;
		return copyMap(c, map, null);
	}

	/**
	 * 拷贝map到class
	 * 
	 * @param c
	 * @param map
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static <T> T fromMap(Class<T> c, Map map) {
		if (c == null || CommonUtil.isEmpty(map))
			return null;
		T t = null;
		try {
			t = c.newInstance();
			fromMap(t, map);
		} catch (InstantiationException e) {
			// e.printStackTrace();
		} catch (IllegalAccessException e) {
			// e.printStackTrace();
		}
		return t;
	}

	/**
	 * 拷贝map到object
	 * 
	 * @param t
	 * @param map
	 */
	@SuppressWarnings("rawtypes")
	public static <T> void fromMap(T t, Map map) {
		if (CommonUtil.isEmpty(t) || CommonUtil.isEmpty(map))
			return;
		PropertyDescriptor[] propertyDescriptors = getPropertyDescriptors(t.getClass());
		Map<String, PropertyDescriptor> m = new HashMap<String, PropertyDescriptor>();
		for (PropertyDescriptor pd : propertyDescriptors) {
			if (isClass(pd))
				continue;
			m.put(pd.getName(), pd);
		}
		Set keys = map.keySet();
		Object value;
		for (Object key : keys) {
			if (key instanceof String) {
				value = map.get(key);
				PropertyDescriptor propertyDescriptor = m.get(key);
				if (CommonUtil.isNotEmpty(propertyDescriptor)) {
					Method method = propertyDescriptor.getWriteMethod();
					Object[] values = new Object[1];
					if (Date.class.isAssignableFrom(propertyDescriptor.getPropertyType()) && value instanceof String) {
						try {
							Field f = t.getClass().getDeclaredField(propertyDescriptor.getName());
							Annotation annotation = f.getAnnotation(DateTimeFormat.class);
							if (CommonUtil.isNotEmpty(annotation) && CommonUtil.isNotEmpty(value) && CommonUtil.isNotEmpty(annotation)) {
								DateTimeFormat dtf = (DateTimeFormat) annotation;
								if (CommonUtil.isNotEmpty(dtf.pattern())) {
									value = DateUtil.parse(dtf.pattern(), (String) value);
								}
							}
						} catch (NoSuchFieldException e) {
							// e.printStackTrace();
						} catch (SecurityException e) {
							// e.printStackTrace();
						} catch (ParseException e) {
							// e.printStackTrace();
						}
					}
					values[0] = value;
					invokeMethod(t, method, values);
					Object invokeMethod = getVlaue(t, propertyDescriptor);
					if (null == invokeMethod) {
						convert(t, value, propertyDescriptor);
					}
				}
			}
		}
	}

	public static boolean isClass(PropertyDescriptor p) {
		return p.getName().equals(classPropertieName) && p.getPropertyType() instanceof java.lang.Class;
	}

	/**
	 * 获取对象所有属性
	 *
	 * @return
	 */
	public static PropertyDescriptor[] getPropertyDescriptors(Class<?> c) {
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(c);
			if (CommonUtil.isEmpty(beanInfo))
				return null;
			return beanInfo.getPropertyDescriptors();
		} catch (IntrospectionException e) {
			// e.printStackTrace();
		}
		return null;
	}

	private static boolean simpleCopy(PropertyDescriptor po, PropertyDescriptor pd) {
		Class<?> propertyTypeO = po.getPropertyType();
		Class<?> propertyTypeD = pd.getPropertyType();
		return (propertyTypeD.equals(propertyTypeO) && !Collection.class.isAssignableFrom(propertyTypeD) && !Map.class.isAssignableFrom(propertyTypeD)) && !isDate(propertyTypeO, propertyTypeD);
	}

	private static void customCopyPropertie(Object dest, Object orig, PropertyDescriptor pd, PropertyDescriptor po) {
		Method readMethod = po.getReadMethod();
		Object value = invokeMethod(orig, readMethod, EMPTY_OBJECT_ARRAY);
		Method writeMethod = pd.getWriteMethod();
		Class<?> propertyType = pd.getPropertyType();
		Class<?> propertyTypeo = po.getPropertyType();
		try {
			if (isArray(propertyType)) {
				copyArray(dest, value, pd);
			} else if (isList(propertyType)) {
				copyList(dest, value, pd);
			} else if (isSet(propertyType)) {
				copySet(dest, value, pd);
			} else if (isMap(propertyType)) {
				copyMap(dest, value, pd);
			} else if (isDate(propertyType, propertyTypeo)) {
				copyDate(dest, orig, value, pd, po);
			} else if (bothPrimitive(propertyType, propertyTypeo)) {
				convert(dest, value, pd);
			} else {
				Object newInstance = propertyType.newInstance();
				Object[] values = new Object[1];
				copyProperties(newInstance, value);
				values[0] = newInstance;
				invokeMethod(dest, writeMethod, values);
			}
		} catch (InstantiationException e) {
			// e.printStackTrace();
		} catch (IllegalAccessException e) {
			// e.printStackTrace();
		}
	}

	private static void convert(Object dest, Object value, PropertyDescriptor pd) {
		Class<?> propertyType = pd.getPropertyType();
		Method writeMethod = pd.getWriteMethod();
		Object[] values = new Object[1];
		if (null == value)
			return;
		if (propertyType == String.class) {
			try {
				String v = String.valueOf(value);
				values[0] = v;
			} catch (Exception e) {
				// e.printStackTrace();
			}
		} else if (propertyType == int.class || propertyType == Integer.class) {
			try {
				Integer v = Integer.parseInt(value + "");
				values[0] = v;
			} catch (Exception e) {
				// e.printStackTrace();
			}
		} else if (propertyType == double.class || propertyType == Double.class) {
			try {
				Double v = Double.parseDouble(value + "");
				values[0] = v;
			} catch (Exception e) {
				// e.printStackTrace();
			}
		} else if (propertyType == float.class || propertyType == Float.class) {
			try {
				Float v = Float.parseFloat(value + "");
				values[0] = v;
			} catch (Exception e) {
				// e.printStackTrace();
			}
		} else if (propertyType == char.class || propertyType == Character.class) {
			try {
				Character v = Character.valueOf((char) value);
				values[0] = v;
			} catch (Exception e) {
				// e.printStackTrace();
			}
		} else if (propertyType == long.class || propertyType == Long.class) {
			try {
				Long v = Long.parseLong(value + "");
				values[0] = v;
			} catch (Exception e) {
				// e.printStackTrace();
			}
		} else if (propertyType == byte.class || propertyType == Byte.class) {
			try {
				Byte v = Byte.valueOf(value + "");
				values[0] = v;
			} catch (Exception e) {
				// e.printStackTrace();
			}
		} else if (propertyType == boolean.class || propertyType == Boolean.class) {
			try {
				Boolean v = Boolean.valueOf(value + "");
				values[0] = v;
			} catch (Exception e) {
				// e.printStackTrace();
			}
		}
		invokeMethod(dest, writeMethod, values);

	}

	private static boolean bothPrimitive(Class<?> propertyType, Class<?> propertyTypeo) {
		return (propertyType.isPrimitive() || propertyType.getName().startsWith("java.lang")) && (propertyTypeo.isPrimitive() || propertyTypeo.getName().startsWith("java.lang"));
	}

	private static void copyDate(Object dest, Object orig, Object value, PropertyDescriptor pd, PropertyDescriptor po) {
		DateTimeFormat annotation;
		Method writeMethod = pd.getWriteMethod();
		if (pd.getPropertyType() == Date.class && po.getPropertyType() == String.class) {
			try {
				Field field = dest.getClass().getDeclaredField(pd.getName());
				annotation = field.getAnnotation(DateTimeFormat.class);
				if (CommonUtil.isEmpty(annotation) || CommonUtil.isEmpty(value))
					return;
				String pattern = annotation.pattern();
				Date date = DateUtil.parse(pattern, String.valueOf(value));
				Object[] values = new Object[1];
				values[0] = date;
				invokeMethod(dest, writeMethod, values);
			} catch (NoSuchFieldException e) {
				// e.printStackTrace();
			} catch (SecurityException e) {
				// e.printStackTrace();
			} catch (ParseException e) {
				// e.printStackTrace();
			}

		} else if (pd.getPropertyType() == String.class && po.getPropertyType() == Date.class) {
			try {
				Field field = orig.getClass().getDeclaredField(po.getName());
				annotation = field.getAnnotation(DateTimeFormat.class);
				if (CommonUtil.isEmpty(annotation) || CommonUtil.isEmpty(value))
					return;
				String pattern = annotation.pattern();
				String str = DateUtil.format(pattern, (Date) value);
				Object[] values = new Object[1];
				values[0] = str;
				invokeMethod(dest, writeMethod, values);
			} catch (NoSuchFieldException e) {
				// e.printStackTrace();
			} catch (SecurityException e) {
				// e.printStackTrace();
			}
		}

	}

	private static boolean isDate(Class<?> propertyType, Class<?> propertyTypeo) {
		return (propertyType == Date.class && propertyTypeo == String.class) || (propertyType == String.class && propertyTypeo == Date.class);
	}

	private static boolean isCollection(Class<?> propertyType) {
		return Collection.class.isAssignableFrom(propertyType);
	}

	private static boolean isMap(Class<?> propertyType) {
		return Map.class.isAssignableFrom(propertyType);
	}

	private static boolean isSet(Class<?> propertyType) {
		return Set.class.isAssignableFrom(propertyType);
	}

	private static boolean isList(Class<?> propertyType) {
		return List.class.isAssignableFrom(propertyType);
	}

	private static boolean isArray(Class<?> propertyType) {
		return propertyType.isArray();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static Map copyMap(Object dest, Object value, PropertyDescriptor pd) {
		if (null == dest || null == value || (CommonUtil.isEmpty(pd) && !isMap(dest.getClass())) || (CommonUtil.isEmpty(pd) && !(dest instanceof Class)))
			return null;
		Map v = (Map) value;
		Map m = new HashMap();
		Iterator entries = v.entrySet().iterator();
		Class genericClazz = CommonUtil.isEmpty(pd) ? (Class) dest : getGenericClazz(dest, pd);
		while (entries.hasNext()) {
			Map.Entry entry = (Map.Entry) entries.next();
			if (CommonUtil.isEmpty(genericClazz) || Object.class.equals(genericClazz))
				m.put(entry.getKey(), entry.getValue());
			else {
				Object newInstance;
				try {
					newInstance = genericClazz.newInstance();
					copyProperties(newInstance, entry.getValue());
					m.put(entry.getKey(), newInstance);
				} catch (InstantiationException e) {
					// e.printStackTrace();
				} catch (IllegalAccessException e) {
					// e.printStackTrace();
				}
			}
		}
		if (CommonUtil.isNotEmpty(pd)) {
			Method writeMethod = pd.getWriteMethod();
			Object[] vs = new Object[1];
			vs[0] = m;
			invokeMethod(dest, writeMethod, vs);
		}
		return m;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static Set copySet(Object dest, Object value, PropertyDescriptor pd) {
		if (null == dest || CommonUtil.isEmpty(value) || !isSet(value.getClass()) || (CommonUtil.isEmpty(pd) && !(dest instanceof Class)))
			return null;
		Set v = (Set) value;
		Set set = new HashSet();
		Class genericClazz = (CommonUtil.isEmpty(pd) ? (Class) dest : getGenericClazz(dest, pd));
		for (Object obj : v) {
			if (CommonUtil.isEmpty(genericClazz) || Object.class.equals(genericClazz))
				set.add(obj);
			else {
				Object newInstance;
				try {
					newInstance = genericClazz.newInstance();
					copyProperties(newInstance, obj);
					set.add(newInstance);
				} catch (InstantiationException | IllegalAccessException e) {
					// e.printStackTrace();
				}
			}
		}
		if (CommonUtil.isNotEmpty(pd)) {
			Method writeMethod = pd.getWriteMethod();
			Object[] vs = new Object[1];
			vs[0] = set;
			invokeMethod(dest, writeMethod, vs);
		}
		return set;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static List copyList(Object dest, Object value, PropertyDescriptor pd) {
		if (dest == null || CommonUtil.isEmpty(value) || !isList(value.getClass()) || (CommonUtil.isEmpty(pd) && !(dest instanceof Class)))
			return null;
		List v = (List) value;
		List list = new ArrayList<>();
		Class genericClazz = (CommonUtil.isEmpty(pd) ? (Class) dest : getGenericClazz(dest, pd));
		for (Object obj : v) {
			if (CommonUtil.isEmpty(genericClazz) || Object.class.equals(genericClazz))
				list.add(obj);
			else {
				Object newInstance;
				try {
					newInstance = genericClazz.newInstance();
					copyProperties(newInstance, obj);
					list.add(newInstance);
				} catch (InstantiationException e) {
					// e.printStackTrace();
				} catch (IllegalAccessException e) {
					// e.printStackTrace();
				}
			}
		}
		if (CommonUtil.isNotEmpty(pd)) {
			Method writeMethod = pd.getWriteMethod();
			Object[] vs = new Object[1];
			vs[0] = list;
			invokeMethod(dest, writeMethod, vs);
		}
		return list;
	}

	private static Object copyArray(Object dest, Object value, PropertyDescriptor pd) {
		if (CommonUtil.isEmpty(value) || !isArray(value.getClass()) || (CommonUtil.isEmpty(pd) && !(dest instanceof Class)))
			return null;
		Object[] v = (Object[]) value;
		Object array = CommonUtil.isEmpty(pd) ? Array.newInstance((Class<?>) dest, v.length) : Array.newInstance(pd.getPropertyType().getComponentType(), v.length);
		int index = 0;
		for (Object obj : v) {
			Object newInstance;
			try {
				newInstance = pd.getPropertyType().getComponentType().newInstance();
				copyProperties(newInstance, obj);
				Array.set(array, index, newInstance);
				index++;
			} catch (InstantiationException e) {
				// e.printStackTrace();
			} catch (IllegalAccessException e) {
				// e.printStackTrace();
			}
		}
		if (CommonUtil.isNotEmpty(pd)) {
			Method writeMethod = pd.getWriteMethod();
			Object[] vs = new Object[1];
			vs[0] = array;
			invokeMethod(dest, writeMethod, vs);
		}
		return array;
	}

	@SuppressWarnings("rawtypes")
	private static Class getGenericClazz(Object dest, PropertyDescriptor pd) {
		if (CommonUtil.isEmpty(dest) || CommonUtil.isEmpty(pd))
			return null;
		Field f = null;
		try {
			f = dest.getClass().getDeclaredField(pd.getName());
		} catch (NoSuchFieldException e1) {
			e1.printStackTrace();
		} catch (SecurityException e1) {
			e1.printStackTrace();
		}
		if (CommonUtil.isEmpty(f))
			return null;
		Class fieldClazz = f.getType();
		if (isPrimitive(fieldClazz))
			return null;
		Type fc = f.getGenericType();
		if (fc == null)
			return null;
		if (fc instanceof ParameterizedType) {
			ParameterizedType pt = (ParameterizedType) fc;
			Class genericClazz = null;
			if (isMap(fieldClazz))
				try {
					genericClazz = (Class) pt.getActualTypeArguments()[1];
				} catch (Exception e) {
					return null;
				}
			else if (isCollection(fieldClazz))
				try {
					genericClazz = (Class) pt.getActualTypeArguments()[0];
				} catch (Exception e) {
					return null;
				}
			if (isPrimitive(genericClazz))
				return null;
			return genericClazz;
		}
		return null;
	}

	@SuppressWarnings("rawtypes")
	private static boolean isPrimitive(Class c) {
		return c.isPrimitive() || c.getName().startsWith("java.lang") || Date.class.isAssignableFrom(c);
	}

	private static void copyPropertie(Object dest, Object orig, PropertyDescriptor pd, PropertyDescriptor po) {
		Method readMethod = po.getReadMethod();
		Object value = invokeMethod(orig, readMethod, EMPTY_OBJECT_ARRAY);
		Method writeMethod = pd.getWriteMethod();
		Object[] values = new Object[1];
		values[0] = value;
		invokeMethod(dest, writeMethod, values);
	}

	/**
	 * 反射执行方法
	 * 
	 * @param orig
	 * @param method
	 * @param values
	 * @return
	 */
	public static Object invokeMethod(Object orig, Method method, Object[] values) {
		if (CommonUtil.isEmpty(method) || CommonUtil.isEmpty(orig))
			return null;
		method.setAccessible(true);
		try {
			return method.invoke(orig, values);
		} catch (IllegalAccessException e) {
			// e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// e.printStackTrace();
		} catch (InvocationTargetException e) {
			// e.printStackTrace();
		}
		return null;
	}

	public static Object getVlaue(Object t, PropertyDescriptor p) {
		if (CommonUtil.isEmpty(t) || CommonUtil.isEmpty(p))
			return null;
		Method method = p.getReadMethod();
		if (CommonUtil.isEmpty(method))
			return null;
		Object value = invokeMethod(t, method, BeanUtils.EMPTY_OBJECT_ARRAY);
		return value;

	}

	/**
	 * 
	 * @param t
	 * @param excludeEmpty
	 *            空值不拷贝,默认false
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Map toMap(Object t, Boolean excludeEmpty) {
		if (CommonUtil.isEmpty(t))
			return null;
		boolean exclude = CommonUtil.isEmpty(excludeEmpty) ? false : excludeEmpty;

		PropertyDescriptor[] propertyDescriptors = getPropertyDescriptors(t.getClass());
		if (CommonUtil.isEmpty(propertyDescriptors))
			return null;
		Map<Object, Object> m = new HashMap<Object, Object>();
		for (PropertyDescriptor p : propertyDescriptors) {
			if (isClass(p))
				continue;
			Object value = getVlaue(t, p);
			if (exclude && CommonUtil.isEmpty(value))
				continue;
			m.put(p.getName(), value);
		}
		return m;

	}

	/**
	 * 
	 * @param t
	 * @param excludeEmpty
	 *            空值不拷贝,默认false
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static TreeMap toTreeMap(Object t, Boolean excludeEmpty) {
		if (CommonUtil.isEmpty(t))
			return null;
		boolean exclude = CommonUtil.isEmpty(excludeEmpty) ? false : excludeEmpty;

		PropertyDescriptor[] propertyDescriptors = getPropertyDescriptors(t.getClass());
		if (CommonUtil.isEmpty(propertyDescriptors))
			return null;
		TreeMap<Object, Object> m = new TreeMap<Object, Object>();
		for (PropertyDescriptor p : propertyDescriptors) {
			if (isClass(p))
				continue;
			Object value = getVlaue(t, p);
			if (exclude && CommonUtil.isEmpty(value))
				continue;
			m.put(p.getName(), value);
		}
		return m;

	}

	/**
	 * 对象转数组
	 * 
	 * @param obj
	 * @return
	 */
	public static byte[] toByteArray(Object obj) {
		if (CommonUtil.isEmpty(obj))
			return null;
		byte[] bytes = null;
		ByteArrayOutputStream bos = null;
		ObjectOutputStream oos = null;
		try {
			bos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(bos);
			oos.writeObject(obj);
			oos.flush();
			bytes = bos.toByteArray();
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (null != oos)
					oos.close();
				if (null != bos)
					bos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return bytes;
	}

	/**
	 * 数组转对象
	 * 
	 * @param bytes
	 * @return
	 */
	public static Object toObject(byte[] bytes) {
		if (CommonUtil.isEmpty(bytes))
			return null;
		Object obj = null;
		ByteArrayInputStream bis = null;
		ObjectInputStream ois = null;
		try {
			bis = new ByteArrayInputStream(bytes);
			ois = new ObjectInputStream(bis);
			obj = ois.readObject();
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (null != ois)
					ois.close();
				if (null != bis)
					bis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return obj;
	}

}
