package com.example.dicom_reader.utils;

import com.example.dicom_reader.pojo.MyDicom;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.io.File;
import java.io.IOException;

import static com.example.dicom_reader.utils.DcmReader.name4imgPath;
import static com.example.dicom_reader.utils.DcmReader.saveProcessImg;

public class DcmReaderGUI extends JFrame {
    private JButton button;
    private JLabel label;
    private DrawPanel drawPanel;
    private TextPanel textPanel;
    private JFileChooser chooser;
    private static final int screenWidth =
            Toolkit.getDefaultToolkit().getScreenSize().width;
    private static final int screenHeight =
            Toolkit.getDefaultToolkit().getScreenSize().height;
    private static final int DEFAULT_WIDTH = 819;
    private static final int DEFAULT_HEIGHT = 614;
    private static final String DEFAULT_PATH =
            "./src/main/resources/static/DICOM/image";
    private static final String DEFAULT_DICOM =
            "./src/main/resources/static/DICOM";
    private static final String ERROR =
            "src/main/resources/static/DICOM/image/error2.png";
    public int processStep = 0;
    public ArrayList<BufferedImage> imageProcess = new ArrayList<BufferedImage>();
    public DcmReader reader = new DcmReader();
    public MyDicom myDicom = reader.getMyDicom();

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
        System.out.println("width: " + width + "\n"
                + "height: " + height);

    }

    public DcmReaderGUI() {
        super();
        setTitle("DcmReader");
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        //居中
        setLocation((screenWidth - DEFAULT_WIDTH) / 2,
                (screenHeight - DEFAULT_HEIGHT) / 2);

        label = new JLabel();
        add(label);

        drawPanel = new DrawPanel();
        getContentPane().add(drawPanel);

        textPanel = new TextPanel();
        getContentPane().add(BorderLayout.SOUTH, textPanel);


        this.chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File(DEFAULT_DICOM));

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

        /*图像增强*/
        JMenu enhancement = new JMenu("图像增强");
        menuBar.add(enhancement);

        JMenuItem brightItem = new JMenuItem("增亮");
        enhancement.add(brightItem);
        brightItem.addActionListener(new processListener(1));

        JMenuItem histEqualize = new JMenuItem("直方图均衡化");
        enhancement.add(histEqualize);
        histEqualize.addActionListener(new processListener(2));

        JMenuItem laplaceEnhance = new JMenuItem("拉普拉斯变化");
        enhancement.add(laplaceEnhance);
        laplaceEnhance.addActionListener(new processListener(3));

        JMenuItem logEnhance = new JMenuItem("对数变换");
        enhancement.add(logEnhance);
        logEnhance.addActionListener(new processListener(4));

        /*图像锐化sharpening*/
        JMenu sharpening = new JMenu("图像锐化");
        menuBar.add(sharpening);

        JMenuItem sharpen = new JMenuItem("锐化");
        sharpening.add(sharpen);
        sharpen.addActionListener(new processListener(5));

        /*图像模糊*/
        JMenu blurred = new JMenu("图像模糊");
        menuBar.add(blurred);

        JMenuItem blur = new JMenuItem("均值滤波");
        blurred.add(blur);
        blur.addActionListener(new processListener(6));

        JMenuItem medianBlur = new JMenuItem("中值滤波");
        blurred.add(medianBlur);
        medianBlur.addActionListener(new processListener(7));

        JMenuItem GaussianBlur = new JMenuItem("高斯滤波");
        blurred.add(GaussianBlur);
        GaussianBlur.addActionListener(new processListener(8));

                /*形态学处理*/
        JMenu morphology = new JMenu("形态学");
        menuBar.add(morphology);

        JMenuItem open = new JMenuItem("开运算");
        morphology.add(open);
        open.addActionListener(new processListener(9));

        JMenuItem close = new JMenuItem("闭运算");
        morphology.add(close);
        close.addActionListener(new processListener(10));

        /*图像分割*/

        JMenu segmentation = new JMenu("图像分割");
        menuBar.add(segmentation);

        JMenuItem thresholdApt = new JMenuItem("自适应阈值法");
        segmentation.add(thresholdApt);
        thresholdApt.addActionListener(new processListener(11));

        /*撤销重做*/
        JMenu undoOption = new JMenu("撤销");
        menuBar.add(undoOption);
        undoOption.addActionListener(new ActionOptions(false));

        JMenu redoOption = new JMenu("重做");
        menuBar.add(redoOption);
        redoOption.addActionListener(new ActDcmListener(true));

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setVisible(true);

        //addComponentsToPane(getContentPane());
    }

    private void addComponentsToPane(Container pane) {


    }

    public void creatGUI() {
        JFrame frame = new JFrame();

        //button = new JButton("click me");
        //button.addActionListener(this);

        DrawPanel panel = new DrawPanel();
        /*关闭window时，停止程序*/
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.getContentPane().add(panel);
        //frame.getContentPane().add(button);
        frame.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        frame.setVisible(true);

    }

    class DrawPanel extends JPanel {
        private Image srcImg;
        private Image destImg;
        private int srcPointX = DEFAULT_WIDTH / 2 - 275;
        private int srcPointY = DEFAULT_HEIGHT / 8;
        private int destPointX = DEFAULT_WIDTH / 2 + 20;
        private int destPointY = DEFAULT_HEIGHT / 8;
        private double width = 256.0;
        private double height = 256.0;

        public DrawPanel() {
            System.out.println("start!");
            System.out.println("srcPoint: " + srcPointX +
                    "destPoint: " + destPointX);
        }

        public void setSrcImg(Image img, int width, int height) {
            this.srcImg = img;
            this.width = width;
            this.height = height;
            repaint();
        }

        public void setDestImg(Image img, int width, int height) {
            this.destImg = img;
            this.width = width;
            this.height = height;
            repaint();
        }

        public void adaptiveImg(){
            double rate = width/height;
            double max = Math.max(this.width, this.height);
            this.width = this.width >= 256?(256*rate):this.width;
            this.height = this.height >= 256?(256*rate):this.height;

        }

        public void paintComponent(Graphics graphics) {
            //System.out.println("execute paint");
            super.paintComponent(graphics);
            adaptiveImg();
            graphics.drawImage(srcImg, srcPointX, srcPointY,
                    (int)width, (int)height, this);
            graphics.drawImage(destImg, destPointX, destPointY,
                    (int)width, (int)height, this);

        }
    }

    /**
     * 显示保存触发监听
     */
    class ActDcmListener implements ActionListener {
        private boolean isSaveFile;

        public ActDcmListener(boolean isSaveFile) {
            this.isSaveFile = isSaveFile;
        }

        //        @Deprecated
        @Override
        public void actionPerformed(ActionEvent event) {
            int result = chooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                String path = chooser.getSelectedFile().getPath();
                BufferedImage bImage = null;
                try {
                    bImage = ImageIO.read(new File(ERROR));
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("fail to load the error_pic");
                }

                if (!isSaveFile) {
                    reader.setDicom(new File(path));
                    reader.openDcmFile();

                    if(reader.getDicom() != null) {
                        bImage = myDicom.getDcmImage();
                        reader.getDcmImage();
                        System.out.println(reader.getImagePath());
                    }
                    drawPanel.setSrcImg(bImage,
                            myDicom.getWidth(),
                            myDicom.getHeight());
                    drawPanel.setDestImg(bImage,
                            myDicom.getWidth(),
                            myDicom.getHeight());

                    imageProcess.add(myDicom.getDcmImage());
                    processStep++;

                } else {
                    try {
                        reader.saveDcmFile(path);
                        String directory = name4imgPath(path)[3];
                        System.out.println(directory);
                        reader.getDcmImage("bmp", directory);

                        String imgPath = directory + "pro_"+
                                name4imgPath(reader.imagePath)[1]+".bmp";
                        saveProcessImg(imageProcess.get(processStep-1),
                                imgPath);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
    }


    /**
     * 图像处理选项
     */
    class processListener implements ActionListener {
        private int option;

        public processListener(int option) {
            this.option = option;
        }

        @Override
        public void actionPerformed(ActionEvent event) {
            BufferedImage destImage = null;
            try {
                destImage = ImageIO.read(new File(ERROR));
            } catch (IOException e) {
                e.printStackTrace();
            }
            switch (option) {
                case 1:
                    destImage = reader.brightening(reader.getImagePath());
                    break;
                case 2:
                    destImage = reader.histEqualize(reader.getImagePath());
                    break;
                case 3:
                    destImage = reader.laplaceEnhance(reader.getImagePath());
                    break;
                case 4:
                    destImage = reader.logEnhance(reader.getImagePath());
                    break;
                case 5:
                    destImage = reader.sharpen(reader.getImagePath());
                    break;
                case 6:
                    destImage = reader.blur(reader.getImagePath());
                    break;
                case 7:
                    destImage = reader.medianBlur(reader.getImagePath());
                    break;
                case 8:
                    destImage = reader.GaussianBlur(reader.getImagePath());
                    System.out.println(destImage);
                    break;
                case 9:
                    destImage = reader.open(reader.getImagePath());
                    break;
                case 10:
                    destImage = reader.close(reader.getImagePath());
                    break;
                case 11:
                    destImage = reader.thresholdApt(reader.getImagePath());
                    break;
                default:
                    break;
            }

            drawPanel.setDestImg(destImage,
                    destImage.getWidth(),
                    destImage.getHeight());

            imageProcess.add(destImage);
            processStep++;
        }

    }

    class ActionOptions implements ActionListener{
        public boolean isRedo = false;

        public ActionOptions(boolean isRedo){
            this.isRedo = isRedo;
        }

        @Override
        public void actionPerformed(ActionEvent event){
            BufferedImage image = imageProcess.get(0);
            if(isRedo){
                processStep = processStep > imageProcess.size()?
                                processStep: processStep++;
                image = imageProcess.get(processStep);
            }else{
                processStep = processStep-- < 0? processStep:processStep--;
            }
            drawPanel.setDestImg(image,image.getWidth(),
                    image.getHeight());
        }

    }

    class TextPanel extends JPanel{
        private float pointX = 0f;
        private float pointY = DEFAULT_HEIGHT / 3f;
        private JLabel[] labelInfo = new JLabel[6];

        public TextPanel(){
            super();
            this.setBackground(new Color(218, 236, 233));
            this.setPreferredSize(new Dimension(DEFAULT_WIDTH,DEFAULT_HEIGHT/3));


        }

    }

    class ExitListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            System.exit(0);
        }
    }


}
