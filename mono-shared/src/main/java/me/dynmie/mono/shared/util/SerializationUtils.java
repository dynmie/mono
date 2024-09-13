package me.dynmie.mono.shared.util;

import java.io.*;

/**
 * A utility class that provides methods for serializing and deserializing objects.
 *
 * @author dynmie
 */
public class SerializationUtils {
    /**
     * Deserialize an object from the given byte array.
     *
     * @param bytes the byte array containing the serialized object
     * @return the deserialized object
     * @throws ClassNotFoundException if the class of the serialized object cannot be found
     * @throws IOException if an I/O error occurs
     */
    public static Object deserialize(byte[] bytes) throws ClassNotFoundException, IOException {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
             ObjectInputStream ois = new ObjectInputStream(bis)) {
            return ois.readObject();
        }
    }

    /**
     * Serialize the given object into a byte array.
     *
     * @param obj the object to serialize
     * @return the byte array containing the serialized object
     * @throws IOException if an I/O error occurs
     */
    public static byte[] serialize(Serializable obj) throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(obj);
            return bos.toByteArray();
        }
    }
}
