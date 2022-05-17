
import com.example.dicom_reader.pojo.MyDicom;
import com.example.dicom_reader.utils.DcmReader;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class DcmReaderGUI extends JFrame {
    private JButton button;
    private JLabel label;
    private DrawPanel drawPanel;
    private  JFileChooser chooser;
    private static final int DEFAULT_WIDTH = 512;
    private static final int DEFAULT_HEIGHT = 768;
    private static final String DEFAULT_PATH = "static/DICOM/image";
    public DcmReader reader = new DcmReader();
    public MyDicom myDicom = reader.getMyDicom();
    private JPanel panel1;

    public static void main(String[] args) {
//        String[] path = {"src/main/resources/static/DICOM/82821227"
//            ,"src/main/resources/static/DICOM/image-002.dcm"
//            ,"src/main/resources/static/DICOM/image-001.dcm"};
//
        DcmReaderGUI gui = new DcmReaderGUI();
//        gui.reader.setDicom(new File(path[2]));
//        gui.reader.openDcmFile();
//        gui.creatGUI();
        int width = gui.reader.getMyDicom().getWidth();
        int height = gui.reader.getMyDicom().getHeight();
        System.out.println("width: "+ width + "\n"
                +"height: " + height);

    }

    public DcmReaderGUI(){
        super();
        setTitle("DcmReader");
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

        label = new JLabel();
        add(label);

        drawPanel = new DrawPanel();
        add(drawPanel);

        this.chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File("."));

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu menu = new JMenu("文件");
        menuBar.add(menu);

        JMenuItem openItem = new JMenuItem("打开文件");
        menu.add(openItem);
        openItem.addActionListener(new ActDcmListener(false));

        JMenuItem saveItem = new JMenuItem("保存文件");
        menu.add(saveItem);
        saveItem.addActionListener(new ActDcmListener(true));

        JMenuItem exitItem = new JMenuItem("退出");
        menu.add(exitItem);
        exitItem.addActionListener(new ExitListener());

        JMenu processOption = new JMenu("图像增强");
        menuBar.add(processOption);

        JMenuItem impressItem = new JMenuItem("增强一");
        processOption.add(impressItem);
        impressItem.addActionListener(new processListener(1));

        JMenuItem grayItem = new JMenuItem("灰度图");
        processOption.add(grayItem);
        grayItem.addActionListener(new processListener(2));

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(DEFAULT_WIDTH,DEFAULT_HEIGHT);
        setVisible(true);

    }

    public void creatGUI(){
        JFrame frame = new JFrame();

        //button = new JButton("click me");
        //button.addActionListener(this);

        DrawPanel panel = new DrawPanel(myDicom.getDcmImage());
        /*关闭window时，停止程序*/
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(panel);
        //frame.getContentPane().add(button);
        frame.setSize(DEFAULT_WIDTH,DEFAULT_HEIGHT);
        frame.setVisible(true);

    }

    class DrawPanel extends JPanel{

        Image img;
        public DrawPanel(){

        }

        public DrawPanel(Image img){
            this.img = img;
            repaint();
            int[] fh = {1,2,3};

        }

        public void setImage(Image img){
            this.img = img;
            repaint();
            int[] fh = {1,2,3};

        }

        public void paintComponent(Graphics graphics){
            super.paintComponent(graphics);
            graphics.drawImage(img, 0, 0, getWidth(), getHeight(), this);
            //graphics.setColor(new Color(255,255,255));
            //graphics.fill3DRect(20,50,100,100,true);
        }
    }

    class ActDcmListener implements ActionListener{
        private boolean isSaveFile;

        public ActDcmListener(boolean isSaveFile){
            this.isSaveFile = isSaveFile;
        }

        @Override
        public void actionPerformed(ActionEvent event){

            int result = chooser.showOpenDialog(null);
            if(result == JFileChooser.APPROVE_OPTION){
                String path = chooser.getSelectedFile().getPath();
                if(!isSaveFile) {
                    reader.setDicom(new File(path));
                    reader.openDcmFile();
                    reader.getDcmImage();
                    drawPanel.setImage(myDicom.getDcmImage());
//                label.setIcon(new ImageIcon(reader.imagePath));
                    System.out.println(reader.getImagePath());
                }else{
                    try {
                        reader.saveDcmFile(path);
                        reader.getDcmImage("bmp", path);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
    }

    //这里有空调 WiFi

    /**
     *  图像处理选项
     */
    class processListener implements ActionListener{
        private int option;

        public processListener(int option){
            this.option = option;
        }

        @Override
        public void actionPerformed(ActionEvent event){
            switch (option){
                case 1:
                    reader.processing();
                    break;
                case 2:
                    try {
                        reader.rgb2gray(reader.getImagePath());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                default:
                    break;
            }
        }

    }
    class ExitListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent event){
            System.exit(0);
        }
    }


}
