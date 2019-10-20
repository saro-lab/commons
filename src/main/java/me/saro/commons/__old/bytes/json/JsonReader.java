package me.saro.commons.__old.bytes.json;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import me.saro.commons.__old.bytes.Converter;

/**
 * json
 * Thread-safe
 * @author		PARK Yong Seo
 * @since		0.1
 */
public class JsonReader {

    /**
     * create json reader
     * @param data
     */
    public JsonReader(Object data) {
        this.data = data;
    }
    
    /**
     * create json reader
     * @param data
     */
    public JsonReader(String json) {
        int arrayIndex = json.indexOf('[');
        int objectIndex = json.indexOf('{');
        Object data = (arrayIndex > -1 && (arrayIndex < objectIndex || objectIndex == -1))
                ? Converter.toMapListByJsonArray(json)
                : Converter.toMapByJsonObject(json);
        this.data = data;
    }

    // synchronized block
    Boolean lock = true;

    // data
    Object data;
    
    /**
     * is Array
     * @return
     */
    public boolean isArray() {
        boolean rv;
        synchronized (lock) {
            rv = List.class.isAssignableFrom(data.getClass());
        }
        return rv;
    }

    /**
     * is Object
     * @return
     */
    public synchronized boolean isObject() {
        boolean rv;
        synchronized (lock) {
            rv = Map.class.isAssignableFrom(data.getClass());
        }
        return rv;
    }

    /**
     * length
     * @return
     * Array == array.length
     * Object == -1
     */
    public int length() {
        if (isArray()) {
            int rv;
            synchronized (lock) {
                rv = list().size();
            }
            return rv;
        }
        return -1;
    }

    /**
     * to list
     * @return
     */
    public List<JsonReader> toList() {
        List<JsonReader> rv;
        synchronized (lock) {
            if (List.class.isAssignableFrom(data.getClass())) {
                rv = list().stream().map(e -> new JsonReader(e)).collect(Collectors.toList());
            } else {
                rv = null;
            }
        }

        if (rv == null) {
            throw new RuntimeException("is not list");
        }

        return rv;
    }

    /**
     * get by index in the list
     * @param index
     * @return
     */
    public JsonReader get(int index) {
        Object rv;
        synchronized (lock) {
            if (List.class.isAssignableFrom(data.getClass())) {
                rv = list().get(index);
            } else {
                rv = null;
            }
        }

        if (rv == null) {
            throw new RuntimeException("is not list");
        }

        return new JsonReader(rv);
    }

    /**
     * get by name in the object
     * @param name
     * @return
     */
    public Object get(String name) {
        Object rv;
        boolean isNotObject = false;
        synchronized (lock) {
            if (Map.class.isAssignableFrom(data.getClass())) {
                rv = map().get(name);

                if (rv != null && (List.class.isAssignableFrom(rv.getClass()) || Map.class.isAssignableFrom(rv.getClass()))) {
                    rv = new JsonReader(rv);
                }

            } else {
                rv = null;
                isNotObject = true;
            }
        }

        if (isNotObject) {
            throw new RuntimeException("is not object");
        }

        return rv;
    }

    /**
     * get String
     * @param name
     * @return
     */
    public String getString(String name) {
        Object rv = get(name);
        return rv != null ? rv.toString() : null;
    }

    public int getInt(String name, int defaultValue) {
        Object rv = get(name);
        try {
            return rv != null ? Integer.parseInt(rv.toString()) : defaultValue;
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * get int
     * @param name
     * @return
     */
    public JsonReader into(String name) {
        Object rv;
        synchronized (lock) {
            rv = map().get(name);
        }
        return new JsonReader(rv);
    }

    /**
     * to json string
     */
    public String toString() {
        String rv;
        synchronized (lock) {
            rv = Converter.toJson(data);
        }
        return rv;
    }

    /**
     * clone
     */
    @Override
    protected Object clone() {
        return new JsonReader(this.data);
    }

    /**
     * equals
     */
    @Override
    public boolean equals(Object obj) {
        // TODO Auto-generated method stub
        return obj != null 
                && obj.getClass().getName().equals(this.getClass().getName())
                && this.toString().equals(obj.toString());
    }

    /**
     * cast list
     * @return
     */
    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> list() {
        return (List<Map<String, Object>>)data;
    }

    /**
     * cast map
     * @return
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> map() {
        return (Map<String, Object>)data;
    }
}
