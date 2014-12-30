package com.example;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.Hashtable;
import java.util.Vector;

public class IO1L {

	/** Creates a new instance of Utility */
	public IO1L() {
	}

	static public String getFileNameFromPath(String path, String delimiter) {

		int length = path.length();

		int index = path.lastIndexOf(delimiter);

		return path.substring(index, length);

	}

	static public boolean fileExists(String path) {
		File file = new File(path);
		return file.exists();
	}

	static public boolean delete(String path) {
		File file = new File(path);
		return file.delete();
	}

	static public long fileLength(String path) {
		File file = new File(path);
		return file.length();
	}

	static public String getTranslatedPath(String path) {

		String originalPathRoot = null;
		String originalPathRest = null;

		if (path.charAt(1) == ':') {
			String[] parts = path.split("\\\\");
			originalPathRest = parts[2] + "/" + parts[3] + "/" + parts[4];
		} else if (path.charAt(0) == '\\' && path.charAt(1) == '\\') {
			String[] parts = path.split("\\\\");
			originalPathRest = parts[5] + "/" + parts[6] + "/" + parts[7];
		} else if (path.charAt(0) == '/') {
			String[] parts = path.split("/");
			originalPathRest = parts[2] + "/" + parts[3] + "/" + parts[4];
		} else {
			System.out.println("can't parse path = " + path);
		}

		String translatedPath = "/music/mbft/" + originalPathRest;

		return translatedPath;
	}

	static public String getRelativePath(String path) {

		String originalPathRoot = null;
		String originalPathRest = null;

		if (path.charAt(1) == ':') {
			originalPathRoot = path.substring(0, 3);
			originalPathRest = path.substring(3, path.length());
		} else if (path.charAt(0) == '\\' && path.charAt(1) == '\\') {
			int index = path.indexOf("\\", 3);
			index = path.indexOf("\\", index + 1);
			originalPathRest = path.substring(index + 1, path.length());
		} else if (path.charAt(0) == '/') {
			int index = path.indexOf("/", 1);
			index = path.indexOf("/", index + 1);
			index = path.indexOf("/", index + 1);
			originalPathRest = path.substring(index + 1, path.length());
		} else {
			System.out.println("can't parse path = " + path);
		}

		return originalPathRest;
	}

	static public String getFileNameFromPath(String path) {

		String name = null;

		int index = path.lastIndexOf("/");

		name = path.substring(index + 1);

		return name;
	}

	static public Object readObject(String path) {

		Object object = null;
		try {
			FileInputStream istream = new FileInputStream(path);
			ObjectInputStream p = new ObjectInputStream(istream);

			object = p.readObject();

			istream.close();

		} catch (Exception e) {
			System.out.println("Error in readObject:  could not read " + path);
			System.out.println(e);
			e.printStackTrace();
		}

		return object;
	}

	static public void writeObject(String path, Object object) {

		try {
			FileOutputStream fileOutputStream = new FileOutputStream(path);
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

			objectOutputStream.writeObject(object);

			fileOutputStream.close();

		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}
	}

	static public byte[] convertObjectToBytes(Object object) {

		byte[] buf = null;
		try {

			// Serialize to a byte array
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutput out = new ObjectOutputStream(bos);
			out.writeObject(object);
			out.close();

			// Get the bytes of the serialized object
			buf = bos.toByteArray();

		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}

		return buf;
	}

	static public Object convertBytesToObject(byte[] bytes) {

		Object object = null;
		try {
			// Deserialize from a byte array
			ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bytes));
			object = (Object) in.readObject();
			in.close();

		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}

		return object;
	}

	static public Object convertBytesToObject(InputStream stream) {

		Object object = null;
		try {
			// Deserialize from a byte array
			ObjectInputStream in = new ObjectInputStream(stream);
			object = (Object) in.readObject();
			in.close();

		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}

		return object;
	}

	static public Vector getWAVPathStrings(String rootWAVDirectoryPath, String fileNameFilterString) {

		Vector wavFilePaths = new Vector();

		System.out.println("rootWAVDirectoryPath = " + rootWAVDirectoryPath);

		File file = new File(rootWAVDirectoryPath);

		Hashtable fileNameHashtable = new Hashtable();
		int numUniqueFileNames = 0;

		int numWAVFiles = 0;
		if (file.isDirectory()) {

			String[] genreDirectoryNames = file.list();

			for (int d = 0; d < genreDirectoryNames.length; d++) {

				if (!genreDirectoryNames[d].equalsIgnoreCase("Incomplete")) {

					String path = rootWAVDirectoryPath + "/" + genreDirectoryNames[d];

					File genreDirectory = new File(path);

					if (genreDirectory.isDirectory()) {

						String[] wavFileNames = genreDirectory.list();

						for (int f = 0; f < wavFileNames.length; f++) {

							String fileName = wavFileNames[f];

							Object result = fileNameHashtable.get(fileName);

							if ((result == null) && (fileName.endsWith(".wav") || fileName.endsWith(".WAV")) && ((fileNameFilterString == null) || (fileName.indexOf(fileNameFilterString) != -1))) {

								wavFilePaths.add(path + "/" + fileName);

								fileNameHashtable.put(fileName, new Integer(numUniqueFileNames++));
							}
						}
					}

				}
			}

		}
		return wavFilePaths;
	}

	static public void getPathStrings(Vector searchDirectoryFileVector, Vector resultDirectoryVector, Vector resultFileVector) {
		getPathStrings(searchDirectoryFileVector, resultDirectoryVector, resultFileVector, true);
	}

	static public void getPathStrings(Vector searchDirectoryFileVector, Vector resultFileVector) {
		getPathStrings(searchDirectoryFileVector, null, resultFileVector, true);
	}

	static public void getPathStrings(Vector searchDirectoryFileVector, Vector resultDirectoryVector, Vector resultFileVector, boolean recursive) {

		if (searchDirectoryFileVector.size() == 0) {
			return;
		}

		while (true) {

			// System.out.println("searchDirectoryFileVector.size() = " + searchDirectoryFileVector.size());

			File directory = (File) searchDirectoryFileVector.remove(0);

			if (resultDirectoryVector != null)
				resultDirectoryVector.add(directory);

			if (directory.isDirectory()) {
				System.out.println("getting file list for: " + directory.getAbsolutePath());
				File[] files = directory.listFiles();

				int numFilesInDirectory = files.length;

				for (int i = 0; i < numFilesInDirectory; i++) {

					File file = files[i];

					if (file.isFile()) {
						resultFileVector.add(file);
					}

					if (file.isDirectory() && recursive) {
						searchDirectoryFileVector.add(file);
					}

				}
			}

			if (searchDirectoryFileVector.size() == 0) {
				break;
			}
		}

	}

	static public int parseDWORD(byte[] buffer, int index) {
		int sum = 0;
		for (int i = 0; i < 4; i++) {
			int byteIntValue = buffer[index];

			// System.out.println("parseDWORD," + i + "," + byteIntValue);

			if (byteIntValue < 0) {
				byteIntValue = 256 + byteIntValue;
			}
			int partialSum = byteIntValue << (i * 8);
			sum += partialSum;
			// System.out.println(index + "," + byteIntValue + "," + partialSum + "," + sum);
			index++;
		}
		return sum;
	}

	static public long parseDWORDLong(byte[] buffer, int index) {
		long sum = 0;
		for (int i = 0; i < 4; i++) {
			long byteLongValue = buffer[index];

			// System.out.println("parseDWORD," + i + "," + byteIntValue);

			if (byteLongValue < 0) {
				byteLongValue = 256 + byteLongValue;
			}
			long partialSum = byteLongValue << (i * 8);
			sum += partialSum;
			// System.out.println(index + "," + byteIntValue + "," + partialSum + "," + sum);
			index++;
		}
		return sum;
	}

	static public int parseWORD(byte[] buffer, int index) {
		int sum = 0;
		for (int i = 0; i < 2; i++) {
			int byteIntValue = buffer[index];

			// System.out.println("parseWORD," + i + "," + byteIntValue);

			if (byteIntValue < 0) {
				byteIntValue = 256 + byteIntValue;
			}
			int partialSum = byteIntValue << (i * 8);
			sum += partialSum;
			// System.out.println(index + "," + byteIntValue + "," + partialSum + "," + sum);
			index++;
		}
		return sum;
	}

	static public int encodeWORD(byte[] buffer, int index, int value) {

		for (int i = 0; i < 2; i++) {

			int byteIntValue = value % 256;
			if (byteIntValue > 127) {
				byteIntValue = byteIntValue - 256;
			}
			buffer[index + i] = (byte) byteIntValue;

			value = value / 256;

			// System.out.println("encodeWORD," + i + "," + byteIntValue);
		}
		return index + 2;
	}

	static public int encodeDWORD(byte[] buffer, int index, int value) {

		for (int i = 0; i < 4; i++) {

			int byteIntValue = value % 256;
			if (byteIntValue > 127) {
				byteIntValue = byteIntValue - 256;
			}
			buffer[index + i] = (byte) byteIntValue;

			value = value / 256;

			// System.out.println("encodeDWORD," + i + "," + byteIntValue);
		}

		return index + 4;
	}

	static public int encodeDWORDlong(byte[] buffer, int index, long value) {

		for (int i = 0; i < 4; i++) {

			int byteIntValue = (int) (value % 256);
			if (byteIntValue > 127) {
				byteIntValue = byteIntValue - 256;
			}
			buffer[index + i] = (byte) byteIntValue;

			value = value / 256;

			// System.out.println("encodeDWORD," + i + "," + byteIntValue);
		}

		return index + 4;
	}

	static public int parseInteger(byte[] buffer, int index) {
		int sum = 0;
		for (int i = 0; i < 4; i++) {
			int byteIntValue = buffer[index];
			// System.out.println("parseInteger," + i + "," + byteIntValue);
			if (byteIntValue < 0) {
				byteIntValue = 256 + byteIntValue;
			}
			int partialSum = byteIntValue << ((3 - i) * 8);
			sum += partialSum;
			// System.out.println(index + "," + byteIntValue + "," + partialSum + "," + sum);
			index++;
		}
		return sum;
	}

	static public int parseShort(byte[] buffer, int index) {
		int sum = 0;
		for (int i = 0; i < 2; i++) {
			int byteIntValue = buffer[index];
			// System.out.println("parseShort," + i + "," + byteIntValue);
			if (byteIntValue < 0) {
				byteIntValue = 256 + byteIntValue;
			}
			int partialSum = byteIntValue << ((1 - i) * 8);
			sum += partialSum;
			// System.out.println(index + "," + byteIntValue + "," + partialSum + "," + sum);
			index++;
		}
		return sum;
	}

	static public int encodeShort(byte[] buffer, int index, int value) {

		for (int i = 0; i < 2; i++) {

			int byteIntValue = value % 256;
			if (byteIntValue > 127) {
				byteIntValue = byteIntValue - 256;
			}
			buffer[index + (1 - i)] = (byte) byteIntValue;

			value = value / 256;

		}

		for (int i = 0; i < 2; i++) {
			// System.out.println("encodeShort," + i + "," + buffer[index + i]);
		}
		return index + 2;
	}

	static public int encodeInteger(byte[] buffer, int index, int value) {

		for (int i = 0; i < 4; i++) {

			int byteIntValue = value % 256;
			if (byteIntValue > 127) {
				byteIntValue = byteIntValue - 256;
			}
			buffer[index + (3 - i)] = (byte) byteIntValue;

			value = value / 256;

		}

		for (int i = 0; i < 4; i++) {
			// System.out.println("encodeInteger," + i + "," + buffer[index + i]);
		}
		return index + 4;
	}

}
