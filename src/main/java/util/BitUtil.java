package util;

/**
 * 用来操作位的工具
 * Created by QPing on 2015/4/13.
 */
public class BitUtil {

    public static int getBitArraySize(int blocks) {
        int size = blocks / Byte.SIZE;
        if (blocks % Byte.SIZE > 0) {
            size++;
        }
        return size;
    }

    public static byte[] createBit(int len) {
        int size = len / Byte.SIZE;
        if (len % Byte.SIZE > 0) {
            size++;
        }
        return new byte[size];
    }

    /**
     * 取出某位，是0 还是1
     */
    public static boolean getBit(byte[] bits, int pos) {
        int i = pos / Byte.SIZE;
        byte b = bits[i];
        int j = pos % Byte.SIZE;

        return checkBitValue(b, j);
    }

    /**
     * 设置某位，是0 还是1
     */
    public static void setBit(byte[] bits, int pos, boolean flag) {
        int i = pos / Byte.SIZE;
        byte b = bits[i];
        int j = pos % Byte.SIZE;

        byte result = setBitValue(b, j, (byte) (flag ? 1 : 0));
        bits[i] = result;
    }

    /**
     * 获取运算数指定位置的值<br>
     * 例如： 0000 1011 获取其第 0 位的值为 1, 第 2 位 的值为 0<br>
     *
     * @param source 需要运算的数
     * @param pos    指定位置 (0<=pos<=7)
     * @return 指定位置的值(0 or 1)
     */
    public static byte getBitValue(byte source, int pos) {
        return (byte) ((source >> pos) & 1);
    }


    /**
     * 将运算数指定位置的值置为指定值<br>
     * 例: 0000 1011 需要更新为 0000 1111, 即第 2 位的值需要置为 1<br>
     *
     * @param source 需要运算的数
     * @param pos    指定位置 (0<=pos<=7)
     * @param value  只能取值为 0, 或 1, 所有大于0的值作为1处理, 所有小于0的值作为0处理
     * @return 运算后的结果数
     */
    public static byte setBitValue(byte source, int pos, byte value) {

        byte mask = (byte) (1 << pos);
        if (value > 0) {
            source |= mask;
        } else {
            source &= (~mask);
        }

        return source;
    }


    /**
     * 将运算数指定位置取反值<br>
     * 例： 0000 1011 指定第 3 位取反, 结果为 0000 0011; 指定第2位取反, 结果为 0000 1111<br>
     *
     * @param source
     * @param pos    指定位置 (0<=pos<=7)
     * @return 运算后的结果数
     */
    public static byte reverseBitValue(byte source, int pos) {
        byte mask = (byte) (1 << pos);
        return (byte) (source ^ mask);
    }


    /**
     * 检查运算数的指定位置是否为1<br>
     *
     * @param source 需要运算的数
     * @param pos    指定位置 (0<=pos<=7)
     * @return true 表示指定位置值为1, false 表示指定位置值为 0
     */
    public static boolean checkBitValue(byte source, int pos) {
        source = (byte) (source >>> pos);
        return (source & 1) == 1;
    }

    public static void main(String[] args) {
        byte[] bits = BitUtil.createBit(9);

        BitUtil.setBit(bits, 0, true);
        BitUtil.setBit(bits, 1, true);
        BitUtil.setBit(bits, 2, false);
        BitUtil.setBit(bits, 3, true);
        BitUtil.setBit(bits, 4, true);
        BitUtil.setBit(bits, 5, false);
        BitUtil.setBit(bits, 6, false);
        BitUtil.setBit(bits, 7, true);
        BitUtil.setBit(bits, 8, true);
        BitUtil.setBit(bits, 9, true);


        System.out.println("0" + BitUtil.getBit(bits, 0));
        System.out.println("1" +BitUtil.getBit(bits, 1));
        System.out.println("2" +BitUtil.getBit(bits, 2));
        System.out.println("3" +BitUtil.getBit(bits, 3));
        System.out.println("4" +BitUtil.getBit(bits, 4));
        System.out.println("5" +BitUtil.getBit(bits, 5));
        System.out.println("6" +BitUtil.getBit(bits, 6));
        System.out.println("7" +BitUtil.getBit(bits, 7));
        System.out.println("8" +BitUtil.getBit(bits, 8));

    }

}
