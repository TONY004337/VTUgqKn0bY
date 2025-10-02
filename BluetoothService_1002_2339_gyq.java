// 代码生成时间: 2025-10-02 23:39:52
import io.javalin.Handler;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRegistrationException;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;

public class BluetoothService {

    private static final UUID MY_UUID = new UUID(0x1101);
    private StreamConnection connection = null;

    // 开启蓝牙服务并监听连接请求
    public void startService() {
        try {
            LocalDevice localDevice = LocalDevice.getLocalDevice();
            DiscoveryAgent agent = localDevice.getDiscoveryAgent();
            agent.startInquiry(DiscoveryAgent.GIAC, new DiscoveryListener() {

                @Override
                public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod) {
                    System.out.println("Device found: " + btDevice.getBluetoothAddress());
                }

                @Override
                public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {
                }

                @Override
                public void serviceSearchCompleted(int transID, int respCode) {
                }

                @Override
                public void inquiryCompleted(int discType) {
                }
            });
        } catch (BluetoothStateException e) {
            e.printStackTrace();
        }
    }

    // 连接到蓝牙设备
    public void connect(RemoteDevice device) {
        try {
            // 建立连接
            connection = (StreamConnection) Connector.open("btspp://" + device.getBluetoothAddress() + ";uuid=" + MY_UUID.toString());
            InputStream in = connection.openInputStream();
            OutputStream out = connection.openOutputStream();

            // 发送数据
            out.write("Hello".getBytes());
            out.flush();

            // 接收数据
            byte[] buffer = new byte[1024];
            int bytes;
            while ((bytes = in.read(buffer)) != -1) {
                System.out.println("Received: " + new String(buffer, 0, bytes));
            }

            // 关闭连接
            in.close();
            out.close();
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 断开连接
    public void disconnect() {
        if (connection != null) {
            try {
                connection.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 在Javalin应用中注册蓝牙服务的路由
    public static void registerRoutes(Handler handler) {
        handler.post("/bluetooth", ctx -> {
            // 处理蓝牙服务请求
            BluetoothService service = new BluetoothService();
            service.startService();
        });
    }
}
