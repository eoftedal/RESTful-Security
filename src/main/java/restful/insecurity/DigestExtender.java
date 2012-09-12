package restful.insecurity;

import java.lang.reflect.Field;

import org.bouncycastle.crypto.digests.GeneralDigest;
import org.bouncycastle.crypto.digests.MD5Digest;

public class DigestExtender {
	
	public byte[] extend(GeneralDigest digester, byte[] hash, byte[] newData)
			throws Exception {
		boolean bigEndian = isBigEndian(digester);
		digester.reset();
		byte[] newDigest = new byte[digester.getDigestSize()];
		int numFields = digester.getDigestSize() / 4;
		for (int i = 0; i < numFields; i++) {
			getField(digester.getClass(), "H" + (i + 1)).set(digester, pack(hash, i * 4, bigEndian));
		}
		getField(GeneralDigest.class, "byteCount").set(digester, 64);
		//printParams(digester);
		digester.update(newData, 0, newData.length);
		digester.doFinal(newDigest, 0);
		return newDigest;
	}

	private boolean isBigEndian(GeneralDigest digester) {
		return !digester.getClass().equals(MD5Digest.class);
	}

	public byte[] getPad(GeneralDigest digester, int dataLengthInCurrentHash) {
		byte[] pad = new byte[64 - dataLengthInCurrentHash];
		pad[0] = (byte) 0x80;
		for (int i = 1; i < pad.length; i++) {
			pad[i] = (byte) 0;
		}
		long bitLength = (dataLengthInCurrentHash << 3);
		boolean bigEndian = isBigEndian(digester);
		unpackWord((int) (bitLength & 0xffffffff), pad, pad.length - (bigEndian ? 4 : 8), bigEndian);
		unpackWord((int) (bitLength >>> 32), pad, pad.length - (bigEndian ? 8 : 4), bigEndian);
		return pad;
	}

	private int pack(byte[] data, int begin, boolean bigEndian) throws Exception {
		int value = 0;
		for (int i = 0; i < 4; i++) {
			int shift = (4 - 1 - i) * 8;
			int ix = bigEndian ? i : (3-i);
			value += (data[ix + begin] & 0x000000FF) << shift;
		}
		return value;
	}

	private Field getField(Class clazz, String name) {
		Field[] fields = clazz.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			if (fields[i].getName().equals(name)) {
				fields[i].setAccessible(true);
				return fields[i];
			}
		}
		throw new IllegalArgumentException("Could not find field with name: " + name);
	}

	private void unpackWord(int word, byte[] out, int outOff, boolean bigEndian) {

		for (int i = 0; i < 4; i++) {
			int ix = bigEndian ? (3-i) : i;
			out[outOff + ix] = (byte) (word >>> i*8);
		}
	}


	public void printParams(GeneralDigest digester) throws Exception {
		System.out.println("Bytecount: "
				+ getField(GeneralDigest.class, "byteCount").get(digester));
		int numFields = digester.getDigestSize() / 4;
		for (int i = 0; i < numFields; i++) {
		System.out.println("H" + (i+1) + ": "
				+ Integer.toHexString((Integer) getField(digester.getClass(), "H" + (i + 1))
						.get(digester)));
		}


	}

}
