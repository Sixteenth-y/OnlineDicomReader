//package com.example.dicom_reader;
//
//import com.example.dicom_reader.pojo.MyDicom;
//
//import javax.imageio.ImageIO;
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.awt.image.BufferedImage;
//import java.util.ArrayList;
//import java.io.File;
//import java.io.IOException;
//
//public class DcmReaderGUI55 extends JFrame {
//    private JButton button;
//    private JLabel label;
//    private DrawPanel drawPanel;
//    private JFileChooser chooser;
//    private static final int screenWidth =
//            Toolkit.getDefaultToolkit().getScreenSize().width;
//    private static final int screenHeight =
//            Toolkit.getDefaultToolkit().getScreenSize().height;
//    private static final int DEFAULT_WIDTH = 819;
//    private static final int DEFAULT_HEIGHT = 614;
//    private static final String DEFAULT_PATH =
//            "./src/main/resources/static/DICOM/image";
//    private static final String DEFAULT_DICOM =
//            "./src/main/resources/static/DICOM";
//    private static final String ERROR =
//            "src/main/resources/static/DICOM/image/error2.png";
//    public int processStep = 0;
//    public ArrayList<BufferedImage> imageProcess = new ArrayList<BufferedImage>();
//    public DcmReader reader = new DcmReader();
//    public MyDicom myDicom = reader.getMyDicom();
//
//    public static void main(String[] args) {
////        String[] path = {"src/main/resources/static/DICOM/82821227"
////            ,"src/main/resources/static/DICOM/image-002.dcm"
////            ,"src/main/resources/static/DICOM/image-001.dcm"};
////
//        DcmReaderGUI gui = new DcmReaderGUI();
////        gui.reader.setDicom(new File(path[2]));
////        gui.reader.openDcmFile();
////        gui.creatGUI();
//        int width = gui.reader.getMyDicom().getWidth();
//        int height = gui.reader.getMyDicom().getHeight();
//        System.out.println("width: " + width + "\n"
//                + "height: " + height);
//
//    }
//
//    public DcmReaderGUI() {
//        super();
//        setTitle("DcmReader");
//        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
//        //居中
//        setLocation((screenWidth - DEFAULT_WIDTH) / 2,
//                (screenHeight - DEFAULT_HEIGHT) / 2);
//
//        label = new JLabel();
//        add(label);
//
//        drawPanel = new DrawPanel();
//        getContentPane().add(drawPanel);
//
//        this.chooser = new JFileChooser();
//        chooser.setCurrentDirectory(new File(DEFAULT_DICOM));
//
//        JMenuBar menuBar = new JMenuBar();
//        setJMenuBar(menuBar);
//
//        JMenu menu = new JMenu("文件");
//        menuBar.add(menu);
//
//        JMenuItem openItem = new JMenuItem("打开文件");
//        menu.add(openItem);
//        openItem.addActionListener(new ActDcmListener(false));
//
//        JMenuItem saveItem = new JMenuItem("保存文件");
//        menu.add(saveItem);
//        saveItem.addActionListener(new ActDcmListener(true));
//
//        JMenuItem exitItem = new JMenuItem("退出");
//        menu.add(exitItem);
//        exitItem.addActionListener(new ExitListener());
//
//        /*图像增强*/
//        JMenu enhancement = new JMenu("图像增强");
//        menuBar.add(enhancement);
//
//        JMenuItem brightItem = new JMenuItem("增亮");
//        enhancement.add(brightItem);
//        brightItem.addActionListener(new processListener(1));
//
//        JMenuItem histEqualize = new JMenuItem("直方图均衡化");
//        enhancement.add(histEqualize);
//        histEqualize.addActionListener(new processListener(2));
//
//        JMenuItem laplaceEnhance = new JMenuItem("拉普拉斯变化");
//        enhancement.add(laplaceEnhance);
//        laplaceEnhance.addActionListener(new processListener(3));
//
//        JMenuItem logEnhance = new JMenuItem("对数变换");
//        enhancement.add(logEnhance);
//        logEnhance.addActionListener(new processListener(4));
//
//        /*图像锐化sharpening*/
//        JMenu sharpening = new JMenu("图像锐化");
//        menuBar.add(sharpening);
//
//        /*图像模糊*/
//        JMenu blur = new JMenu("图像模糊");
//        menuBar.add(blur);
//
//        /*形态学处理*/
//        JMenu morphology = new JMenu("形态学");
//        menuBar.add(morphology);
//
//        /*图像分割*/
//
//        JMenu segmentation = new JMenu("图像分割");
//        menuBar.add(segmentation);
//
//        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
//        setVisible(true);
//
//        //addComponentsToPane(getContentPane());
//    }
//
//    private void addComponentsToPane(Container pane) {
//
//
//    }
//
//    public void creatGUI() {
//        JFrame frame = new JFrame();
//
//        //button = new JButton("click me");
//        //button.addActionListener(this);
//
//        DrawPanel panel = new DrawPanel();
//        /*关闭window时，停止程序*/
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.getContentPane().add(panel);
//        //frame.getContentPane().add(button);
//        frame.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
//        frame.setVisible(true);
//
//    }
//
//    class DrawPanel extends JPanel {
//        private Image srcImg;
//        private Image destImg;
//        private int srcPointX = DEFAULT_WIDTH / 2 - 275;
//        private int srcPointY = DEFAULT_HEIGHT / 4;
//        private int destPointX = DEFAULT_WIDTH / 2 + 20;
//        private int destPointY = DEFAULT_HEIGHT / 4;
//        private double width = 256.0;
//        private double height = 256.0;
//
//        public DrawPanel() {
//            System.out.println("start!");
//            System.out.println("srcPoint: " + srcPointX +
//                    "destPoint: " + destPointX);
//        }
//
//        public void setSrcImg(Image img, int width, int height) {
//            this.srcImg = img;
//            this.width = width;
//            this.height = height;
//            repaint();
//        }
//
//        public void setDestImg(Image img, int width, int height) {
//            this.destImg = img;
//            this.width = width;
//            this.height = height;
//            repaint();
//        }
//
//        public void adaptiveImg(){
//            double rate = width/height;
//            double max = Math.max(this.width, this.height);
//            this.width = this.width >= 256?(256*rate):this.width;
//            this.height = this.height >= 256?(256*rate):this.height;
//
//        }
//
//        public void paintComponent(Graphics graphics) {
//            //System.out.println("execute paint");
//            super.paintComponent(graphics);
//            adaptiveImg();
//            graphics.drawImage(srcImg, srcPointX, srcPointY,
//                    (int)width, (int)height, this);
//            graphics.drawImage(destImg, destPointX, destPointY,
//                    (int)width, (int)height, this);
//
//        }
//    }
//
//    class ActDcmListener implements ActionListener {
//        private boolean isSaveFile;
//
//        public ActDcmListener(boolean isSaveFile) {
//            this.isSaveFile = isSaveFile;
//        }
//
//        //        @Deprecated
//        @Override
//        public void actionPerformed(ActionEvent event) {
//            int result = chooser.showOpenDialog(null);
//            if (result == JFileChooser.APPROVE_OPTION) {
//                String path = chooser.getSelectedFile().getPath();
//                BufferedImage bImage = null;
//                try {
//                    bImage = ImageIO.read(new File(ERROR));
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    System.out.println("fail to load the error_pic");
//                }
//
//                if (!isSaveFile) {
//                    reader.setDicom(new File(path));
//                    reader.openDcmFile();
//                    if (reader.getDicom()!= null) {
//                        bImage = myDicom.getDcmImage();
//                        imageProcess.add(myDicom.getDcmImage());
//                        processStep++;
//                        System.out.println(reader.getImagePath());
//                    }
//                    drawPanel.setSrcImg(bImage,
//                            myDicom.getWidth(),
//                            myDicom.getHeight());
//                    drawPanel.setDestImg(bImage,
//                            myDicom.getWidth(),
//                            myDicom.getHeight());
//
//                } else {
//                    try {
//                        reader.saveDcmFile(path);
//                        reader.getDcmImage("bmp", path);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//            }
//        }
//    }
//
//
//    /**
//     * 图像处理选项
//     */
//    class processListener implements ActionListener {
//        private int option;
//
//        public processListener(int option) {
//            this.option = option;
//        }
//
//        @Override
//        public void actionPerformed(ActionEvent event) {
//            BufferedImage destImage = null;
//            try {
//                destImage = ImageIO.read(new File(ERROR));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            switch (option) {
//                case 1:
//                    destImage = reader.brightening(reader.getImagePath());
//                    break;
//                case 2:
//                    destImage = reader.histEqualize(reader.getImagePath());
//                    break;
//                case 3:
//                    destImage = reader.laplaceEnhance(reader.getImagePath());
//                    break;
//                case 4:
//                    destImage = reader.logEnhance(reader.getImagePath());
//                    break;
//                default:
//                    break;
//            }
//
//            drawPanel.setDestImg(destImage,
//                    destImage.getWidth(),
//                    destImage.getHeight());
//
//            imageProcess.add(destImage);
//            processStep++;
//        }
//
//    }
//
//    class ExitListener implements ActionListener {
//        @Override
//        public void actionPerformed(ActionEvent event) {
//            System.exit(0);
//        }
//    }
//}
