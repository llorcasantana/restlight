package restlight.body;

import java.lang.reflect.Field;

public class FormBodyAdapter extends restlight.FormBody {
  
  public static FormBodyAdapter of(Object obj, String... keys) {
    FormBodyAdapter body = new FormBodyAdapter();
    body.addObject(obj, keys);
    return body;
  }
  
  public FormBodyAdapter addObject(Object obj, String... keys) {
    Field[] fields = obj.getClass().getDeclaredFields();
    try {
      for (java.lang.reflect.Field field : fields) {
        String name = field.getName();
        if (apply(keys, name)) {
          this.add(name, field.get(obj));
        }
      }
    } catch (Exception ignore) {
    }
    return this;
  }

  private static boolean apply(String[] keys, String name) {
    if (keys == null || keys.length == 0) {
      return Boolean.TRUE;
    }

    for (String string : keys) {
      if (string != null && string.equals(name)) {
        return Boolean.TRUE;
      }
    }

    return Boolean.FALSE;
  }
}
