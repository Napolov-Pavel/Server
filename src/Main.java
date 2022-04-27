import java.io.IOException;
import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class Main {
    public static void main(String[] args) throws IOException {
        final ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.bind(new InetSocketAddress("localhost", 23334));
        while (true) {
            try (SocketChannel socketChannel = serverChannel.accept()) {
                final ByteBuffer inputBuffer = ByteBuffer.allocate(2 << 10);
                while (socketChannel.isConnected()) {
                    int bytesCount = socketChannel.read(inputBuffer);
                    if (bytesCount == -1) {
                        break;
                    }
                    final String msg = new String(inputBuffer.array(), 0, bytesCount, StandardCharsets.UTF_8);
                    BigInteger numberFibonacci = fibonacci(Integer.parseInt(msg));
                    inputBuffer.clear();
                    System.out.println("Получено сообщение от клиента: " + msg);
                    socketChannel.write(ByteBuffer.wrap(("Число: " + msg + " по фибоначчи: " + numberFibonacci).getBytes(StandardCharsets.UTF_8)));
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
    public static BigInteger[][] matrixMultiplication(BigInteger[][] a, BigInteger[][] b) {
        return new BigInteger[][]{
                {a[0][0].multiply(b[0][0]).add(a[0][1].multiply(b[1][0])), a[0][0].multiply(b[0][1]).add(a[0][1].multiply(b[1][1]))},
                {a[1][0].multiply(b[0][0]).add(a[1][1].multiply(b[1][0])), a[1][0].multiply(b[0][1]).add(a[1][1].multiply(b[1][1]))},
        };
    }

    public static BigInteger[][] matrixPowerFast(BigInteger[][] a, int n) {
        if (n == 0) {
            return new BigInteger[][]{
                    {BigInteger.ONE, BigInteger.ZERO},
                    {BigInteger.ZERO, BigInteger.ONE}
            };
        } else if (n % 2 == 0) {
            return matrixPowerFast(matrixMultiplication(a, a), n / 2);
        } else {
            return matrixMultiplication(matrixPowerFast(a, n - 1), a);
        }
    }

    public static BigInteger fibonacci(int n) {
        if (n == 0) {
            return BigInteger.ZERO;
        }
        BigInteger[][] a = {
                {BigInteger.ONE, BigInteger.ONE},
                {BigInteger.ONE, BigInteger.ZERO}
        };
        BigInteger[][] powerOfA = matrixPowerFast(a, n - 1);
        BigInteger nthFibonacci = powerOfA[0][0];
        return nthFibonacci;
    }
}
