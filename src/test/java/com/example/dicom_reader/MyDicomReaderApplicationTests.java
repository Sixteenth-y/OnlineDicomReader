package com.example.dicom_reader;

import com.example.dicom_reader.pojo.MyDicom;
import com.example.dicom_reader.utils.DcmReader;
import ij.plugin.DICOM;
import org.dcm4che3.data.Attributes;
import org.dcm4che3.data.Tag;
import org.dcm4che3.data.UID;
import org.opencv.highgui.HighGui;
import org.dcm4che3.imageio.codec.Decompressor;
import org.dcm4che3.imageio.codec.Transcoder;
import org.dcm4che3.imageio.plugins.dcm.DicomImageReadParam;
import org.dcm4che3.io.DicomInputStream;
import org.dcm4che3.io.DicomOutputStream;
import org.dcm4che3.tool.dcm2jpg.Dcm2Jpg;
import org.junit.jupiter.api.Test;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.util.Iterator;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;

import static org.opencv.imgcodecs.Imgcodecs.imwrite;

@SpringBootTest
class MyDicomReaderApplicationTests {

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        URL url = ClassLoader.getSystemResource("lib/opencv/opencv_java440.dll");
        System.load(url.getPath());
        System. setProperty("java.awt.headless", "false");
    }

    private String IMAGE_DIRECTORY = "src/main/resources/static/DICOM/image/temp/";

    @Resource
    DcmReader reader;
    String[] path = {"src/main/resources/static/DICOM/image/error.jpg",
            "src/main/resources/static/DICOM/image-00000.dcm",
            "src/main/resources/static/DICOM/image-00001.dcm",
            "src/main/resources/static/DICOM/82821227.dcm",
            "src/main/resources/static/DICOM/image/image-00000.bmp",
            "src/main/resources/static/DICOM/ucimage-00001.dcm"};

    String[] pathImage = {"src/main/resources/static/DICOM/image/image-00000.jpg",
            "src/main/resources/static/DICOM/image/82821227.jpg"};

    private static final int SKELETON = 0;/*骨骼*/
    private static final int THORACIC_CAVITY = 1;/*胸腔*/
    private static final int LUNG = 2;/*肺部*/
    private static final int ABDOMEN = 3;/*腹部*/
    private static final String DEFAULT_PATH = "src/main/resources/static/DICOM/default";
    private static final String DEFAULT_IMAGE_PATH = "src/main/resources/static/DICOM/image/";
    private static final String DEFAULT_DICOM_PATH = "src/main/resources/static/DICOM/";
    private static final String DEFAULT_TEMP_PATH = "src/main/resources/static/DICOM/image/temp/";
    private static final String ERROR =
            "src/main/resources/static/DICOM/image/error.jpg";
    private File dicom = null;
    private static final String BMP = "bmp";
    private static final String JPG = "jpg";
    private static MyDicom myDicom = new MyDicom();
    public String imagePath = "";

    public String[] name4imgPath(String imgPath){
        imgPath = imgPath.replaceAll("\\\\","/");
        String[] result = new String[3];

        String fileName = imgPath.substring(imgPath.lastIndexOf("/")+1);
        result[0] = fileName;

        result[1] = fileName.substring(0,fileName.lastIndexOf(".")+1);

        String format = imgPath.substring(imgPath.lastIndexOf(".")+1);
        result[2] = format;

        return result;
    }


    @Test
    void contextLoads() {
    }

    @Test
    void gary(){
        try {
            reader.rgb2gray(path[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void brighteningTest(){
        String imgPath = pathImage[1];
        float alpha = 1.5f;
        float beta = 10;
        BufferedImage bImage = null;

        Mat image = Imgcodecs.imread(imgPath);
        //Mat imgDst = image.clone();
        Mat imgDst = new Mat(image.rows(), image.cols(), image.type());

        String fileName = name4imgPath(imgPath)[0];
        //g(x,y) = f(x,y) * alpha + beta
        image.convertTo(imgDst, -1, alpha, beta);

        String tempFileName = DEFAULT_TEMP_PATH + "bright-" + fileName;
        Imgcodecs.imwrite(tempFileName, imgDst);

        try {
            bImage = ImageIO.read(new File(tempFileName));
        } catch (IOException e) {
            e.printStackTrace();
        }

        HighGui.imshow("Processed Image", imgDst);
        HighGui.waitKey();
    }

    @Test
    void getFormat(){
            ArrayList<String> list = new ArrayList();
            Collections.addAll(list, ImageIO.getReaderFileSuffixes());
    }

    @Test
    public void myReader(){
        String[] path = {"src/main/resources/static/DICOM/image-00000.dcm",
                "src/main/resources/static/DICOM/image-00001.dcm",
                "src/main/resources/static/DICOM/82821227.dcm"};

        File dcmFile = new File("src/main/resources/static/DICOM/image-00000.dcm");
        try {
            BufferedImage image = ImageIO.read(dcmFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void convertImage(){
        Dcm2Jpg convertUtil = new Dcm2Jpg();
        try {
            convertUtil.initImageWriter("JPEG",null,"com.sun.imageio.plugins.*",null,1l);
            convertUtil.convert(new File(path[3]), new File(pathImage[1]));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void convertImageByIj(){

        try {
            DICOM dicom = new DICOM();
            dicom.run(path[1]);
            BufferedImage bufferedImage = (BufferedImage) dicom.getImage();
            File destFile = new File(pathImage[0]);
            ImageIO.write(bufferedImage,"jpg",destFile);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void transferSyntaxTest(){

        try {
            DicomInputStream dcmIS = new DicomInputStream(new File(path[5]));
            Attributes attrs = dcmIS.readDataset();
            String ts = attrs.getString(Tag.TransferSyntaxUID);
            System.out.println(ts);
            System.out.println("again "+dcmIS.getTransferSyntax());
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void decompress()throws IOException {
        File srcFile = new File(path[2]);
        String pathNew = path[2].replace(srcFile.getName(), "dec_" + srcFile.getName());
        File destFile = new File(pathNew);
        Transcoder transcoder = new Transcoder(srcFile);
        transcoder.setDestinationTransferSyntax(UID.ExplicitVRLittleEndian);
        transcoder.transcode(new Transcoder.Handler() {
            @Override
            public OutputStream newOutputStream(Transcoder transcoder, Attributes dataset) throws IOException {
                return new FileOutputStream(destFile);
            }
        });
    }

    @Test
    public void decompressByS()throws IOException {
        File srcFile = new File(path[3]);
        Attributes attrs = new DicomInputStream(srcFile).readDataset();
        String pathNew = srcFile.getPath().replace(srcFile.getName(),"Dec-"+srcFile.getName());
        File destFile = new File(pathNew);
        //Decompressor decompressor = decompress(attrs,"1.2.840.10008.1.2.1");
        ;
    }

    @Test
    public void myReaderV2(){
        String[] path = {"src/main/resources/static/DICOM/image-00000.dcm",
                "src/main/resources/static/DICOM/image-00001.dcm",
                "src/main/resources/static/DICOM/82821227.dcm"};

        File dcmFile = new File("src/main/resources/static/DICOM/image-00000.dcm");


        try {
            DicomInputStream dcmIS = new DicomInputStream(dcmFile);
            Attributes fmi = dcmIS.readFileMetaInformation();
            Attributes attrs = dcmIS.readDataset();

            byte[] data = attrs.getBytes(Tag.PixelData);
            InputStream in = new ByteArrayInputStream(data);
            BufferedImage image = ImageIO.read(in);
            ImageIO.write(image,"jpg",new File("src/main/resources/static/DICOM/image/image-00000.bmp"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void convert() throws IOException {
        File src = new File("src/main/resources/static/DICOM/image-00000.dcm");
        File dest = new File("src/main/resources/static/DICOM/image/image-00000.bmp");
        if (dest.getParent() != null) {
            File dir = new File(dest.getParent());
            if (!dir.exists()) {
                dir.mkdirs();
            }
        }
        Iterator<ImageReader> iter = ImageIO.getImageReadersByFormatName("DICOM");
        ImageReader reader = iter.next();
        DicomImageReadParam param = (DicomImageReadParam) reader.getDefaultReadParam();
//        param.setWindowCenter(center);
//        param.setWindowWidth(width);
//        param.setVoiLutFunction(vlutFct);
//        param.setPresentationState(prState);
//        param.setPValue2Gray(pval2gray);
//        param.setAutoWindowing(autoWindowing);
        ImageInputStream iis = ImageIO.createImageInputStream(src);
        BufferedImage bi;
        OutputStream out = null;
        try {
            reader.setInput(iis, false);
            bi = reader.read(0, param);
            if (bi == null) {
                System.out.println("\nError: " + src + " - couldn't read!");
                return;
            }
            out = new BufferedOutputStream(new FileOutputStream(dest));
            //JPEGImageEncoder enc = JPEGCodec.createJPEGEncoder(out);
            //enc.encode(bi);
        } finally {

            iis.close();;
            if(out != null)
            out.close();;
        }
    }
    @Test
    public void saveDcmFile() throws IOException {
        FileInputStream dcmIS = new FileInputStream(
                new File(path[3]));
        String path = "D:\\User\\Desktop\\test.dcm";
        File dcmFile = new File(path);
        System.out.println("dcmSrc:"+new DicomInputStream(new File(this.path[3])));
        System.out.println("src:"+ dcmIS.available());
        byte[] buffer = new byte[1024*8];
        FileOutputStream dcmOS = new FileOutputStream(dcmFile);
        int len = 0;
        while((len = dcmIS.read(buffer))!=-1)//循环读取数据
        {
            dcmOS.write(buffer,0,len);
        }

        System.out.println("[success] completely open image!");

        dcmIS.close();
        dcmOS.close();

    }

    @Test
    public void loadTest(){
        try{
            File file = new File("src/main/resources/static/DICOM/image-00000.dcm");
            DicomInputStream dcmIS = new DicomInputStream(file);
            DcmReader reader =  new DcmReader();
            BufferedImage image =  reader.loadTest(dcmIS);
            int width = image.getWidth();
            int height = image.getHeight();
            ImageIO.write(image,"bmp", new File("src/main/resources/static/DICOM/default/test.bmp"));

        } catch(Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void briTest(){
        String path = IMAGE_DIRECTORY+"test.bmp";

        BufferedImage image = reader.brightening(path);
        String dstPath = path.substring(path.lastIndexOf("/")+1);
        dstPath = IMAGE_DIRECTORY+"bright-"+dstPath;
        File file = new File(dstPath);
        try {
            ImageIO.write(image,"bmp",file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
