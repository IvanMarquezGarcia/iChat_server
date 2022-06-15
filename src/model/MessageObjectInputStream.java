package model;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;

public class MessageObjectInputStream extends ObjectInputStream {
	
	public MessageObjectInputStream(InputStream in) throws IOException {
		super(in);
	}

	
	@Override
	protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
		return Mensaje.class;
	}

}
