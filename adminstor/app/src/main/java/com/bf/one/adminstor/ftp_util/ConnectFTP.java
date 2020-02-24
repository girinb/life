package com.bf.one.adminstor.ftp_util;

import android.util.Log;

import com.bf.one.adminstor.MainActivity;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class ConnectFTP {
    private final String TAG = "Connect FTP";
    public FTPClient mFTPClient = null;
//    String host = "girinb.gonetis.com";

//        String host = "183.104.107.160";
    String host = "han-cons.iptime.org";

    String username = "test_id";
    String password = "1945";
    int port = 21;

    public ConnectFTP() {
        mFTPClient = new FTPClient();
    }

    public boolean ftpConnect() {
        boolean result = false;
        try {
            mFTPClient.connect(host, port);

            if (FTPReply.isPositiveCompletion(mFTPClient.getReplyCode())) {
                result = mFTPClient.login(username, password);
                mFTPClient.enterLocalPassiveMode();
//                mFTPClient.enterLocalActiveMode();
            }
        } catch (Exception e) {
            Log.d(TAG, "Couldn't connect to host");
        }
        return result;
    }

    public boolean ftpDisconnect() {
        boolean result = false;
        try {
            mFTPClient.logout();
            mFTPClient.disconnect();
            result = true;
        } catch (Exception e) {
            Log.d(TAG, "Failed to disconnect with server");
        }
        return result;
    }

    public String ftpGetDirectory() {
        String directory = null;
        try {
            directory = mFTPClient.printWorkingDirectory();
        } catch (Exception e) {
            Log.d(TAG, "Couldn't get current directory");
        }
        return directory;
    }

    public boolean ftpChangeDirctory(String directory) {
        try {
            mFTPClient.changeWorkingDirectory(directory);
            return true;
        } catch (Exception e) {
            Log.d(TAG, "Couldn't change the directory");
        }
        return false;
    }

    public String[] ftpGetFileList(String directory) {
        String[] fileList = null;
        int i = 0;
        try {
            FTPFile[] ftpFiles = mFTPClient.listFiles(directory);
            fileList = new String[ftpFiles.length];
            for (FTPFile file : ftpFiles) {
                String fileName = file.getName();

                if (file.isFile()) {
                    fileList[i] = "(File) " + fileName;
                } else {
                    fileList[i] = "(Directory) " + fileName;
                }

                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileList;
    }

    //    public String[] ftpGetFilesize(String directory) {
//        String[] fileList = null;
//        int i = 0;
//        try {
//            FTPFile[] ftpFiles = mFTPClient.listFiles(directory);
//            fileList = new String[ftpFiles.length];
//            for(FTPFile file : ftpFiles) {
//                String fileName = file.getName();
//
//                if (file.isFile()) {
//                    fileList[i] = "(File) " + fileName;
//                } else {
//                    fileList[i] = "(Directory) " + fileName;
//                }
//
//                i++;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return fileList;
//    }
    public boolean ftpCreateDirectory(String directory) {
        boolean result = false;
        try {
            result = mFTPClient.makeDirectory(directory);
        } catch (Exception e) {
            Log.d(TAG, "Couldn't make the directory");
        }
        return result;
    }

    public boolean ftpDeleteDirectory(String directory) {
        boolean result = false;
        try {
            result = mFTPClient.removeDirectory(directory);
        } catch (Exception e) {
            Log.d(TAG, "Couldn't remove directory");
        }
        return result;
    }

    public boolean ftpDeleteFile(String file) {
        boolean result = false;
        try {
            result = mFTPClient.deleteFile(file);
        } catch (Exception e) {
            Log.d(TAG, "Couldn't remove the file");
        }
        return result;
    }

    public boolean ftpRenameFile(String from, String to) {
        boolean result = false;
        try {
            result = mFTPClient.rename(from, to);
        } catch (Exception e) {
            Log.d(TAG, "Couldn't rename file");
        }
        return result;
    }

    public boolean ftpDownloadFile(String srcFilePath, String desFilePath) {
        boolean result = false;
        try {
            mFTPClient.setFileType(FTP.BINARY_FILE_TYPE);
            mFTPClient.setFileTransferMode(FTP.BINARY_FILE_TYPE);

            FileOutputStream fos = new FileOutputStream(desFilePath);

            result = mFTPClient.retrieveFile(srcFilePath, fos);
            fos.close();
        } catch (Exception e) {
            Log.d(TAG, "Download failed");
        }
        return result;
    }

    public boolean ftpUploadFile(String srcFilePath, String desFileName, String desDirectory) {
        boolean result = false;
        try {
            FileInputStream fis = new FileInputStream(srcFilePath);
            if (ftpChangeDirctory(desDirectory)) {
                result = mFTPClient.storeFile(desFileName, fis);
            }
            fis.close();
        } catch (Exception e) {
            Log.d(TAG, "Couldn't upload the file");
        }
        return result;
    }

    public void ftpinit() {
//        mFTPClient.setControlEncoding("eur-kr or utf-8");
        mFTPClient.setControlEncoding("utf-8");
        mFTPClient.setConnectTimeout(10 * 1000);

    }

    //    public boolean ftpsync(String srcFilePath, String desFilePath) {
    public boolean ftpsync(String fileName, String dir) {
        boolean result = false;
        try {
            mFTPClient.setFileType(FTP.BINARY_FILE_TYPE);
            mFTPClient.setFileTransferMode(FTP.BINARY_FILE_TYPE);
            long fileSize = 0;

            FTPFile[] files = mFTPClient.listFiles(dir); //ftp list

            //TODO 파일 구성 순서
            //1. 파일명을 받아서 하드에 파일이 있는지 검사 하고 용량 체크
            //2. 파일이 없으면 무조건 다운로드
            //3. 파일이 있다면 용량이 같은지 체크
            //4. 용량이 다르면 다운로드
            //5. 완성이다 시바
            File oFile = new File(MainActivity.mContext.getCacheDir() + dir + fileName);

            if (oFile.exists()) {
                long lFileSize = oFile.length();
                for (int i = 0; i < files.length; i++) {
                    if (fileName.equals(files[i].getName())) {
                        fileSize = files[i].getSize();
                    }
                }
                if (lFileSize != fileSize) {
                    ftpDownloadFile(dir + fileName, MainActivity.mContext.getCacheDir() + dir + fileName);
                    Log.w("파일 다운", lFileSize + "//" + fileSize + dir + fileName + "파일 다운1");

                }
            } else {
                ftpDownloadFile(dir + fileName, MainActivity.mContext.getCacheDir() + dir + fileName);
                Log.w("파일 다운", dir + fileName + "파일 다운2");
            }
        } catch (Exception e) {
            Log.d(TAG, "Download failed" + e.toString());
        }
        return result;
    }

    public boolean ftpallsync(String fileName, String dir) {
        boolean result = false;
        try {
            mFTPClient.setFileType(FTP.BINARY_FILE_TYPE);
            mFTPClient.setFileTransferMode(FTP.BINARY_FILE_TYPE);
            //TODO 파일 구성 순서
            //1. 파일명을 받아서 하드에 파일이 있는지 검사 하고 용량 체크
            //2. 파일이 없으면 무조건 다운로드
            //3. 파일이 있다면 용량이 같은지 체크
            //4. 용량이 다르면 다운로드
            //5. 완성이다 시바
            ftpDownloadFile(dir + fileName, MainActivity.mContext.getCacheDir() + dir + fileName);

        } catch (Exception e) {
            Log.d(TAG, "Download failed" + e.toString());
        }
        return result;
    }
}
