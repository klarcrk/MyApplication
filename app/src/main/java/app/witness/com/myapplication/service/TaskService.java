/*
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package app.witness.com.myapplication.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by apple on 15/12/11.
 */
public class TaskService extends Service {

    private final IBinder myBinder = new MyBinder();
    //检测版本的URL地址，返回服务器版本信息和下载地址
    private final String serverURL = "http://120.25.203.91/app/app.html";
    //门禁app包名称
    private final String packageName = "com.doto.mymenjin";
    Timer timer;
    //更新时间 小时，1点至5点更新，随机获取
    private int iUpdateHour = 4;
    private int iUpdatMeinute = 10;
    Context context=this;
    Object obj  = new Object();
    /**
	 * 线程的数量
	 */
	private int threadCount = 3;

	/**
	 * 每个下载区块的大小
	 */
	private long blocksize;

	/**
	 * 正在运行的线程的数量
	 */
	private  int runningThreadCount;
    //计时器
    TimerTask task = new TimerTask() {
        public void run() {
            Calendar c = Calendar.getInstance();
            int thisHour = c.get(Calendar.HOUR_OF_DAY);
            int thisMinute = c.get(Calendar.MINUTE);

            //检测是否安装门禁App
            boolean isInstall = isAvilible();
            if (isInstall) {
                System.out.println("APP已安装。");
                //检测app状态，如果没有运行则运行
                boolean isRuning = runClient(getApplicationContext());
                if (!isRuning) {
                    System.out.println("没有在前台运行,正在启动...");
                    startAPP(packageName);
                } else {
                    boolean isInBackground = isBackground(getApplicationContext());
                    if (isInBackground) {
                        System.out.println("在后台运行中...");
                     //   startAPP(packageName);
                    }
                    System.out.println("跳出else...");
                }
                
//                if (thisMinute % 10 != 0) return;
                if (!(thisHour==iUpdateHour && thisMinute==iUpdatMeinute)){
                    //重新随机下次更新时间
                    return;
                }
                System.out.println("进入....");
            } else {
                System.out.println("APP未安装。");
            }
            System.out.println("开始检测更新...");
            RandomTime();
            new myTaskThread(context).start();

        }
    };
    //apk文件下载地址
    private String updateURL = "";
    //下载好的apk文件
    private String apkFilePath = "";

    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }

    public class MyBinder extends Binder implements IService {

        @Override
        public void callMethodInService() {
            new myTaskThread(context).start();
        }

    }
    // 调用startService方法启动Service时调用该方法
    @Override
    public void onStart(Intent intent, int startId) {
        System.out.println("service start");
        String SDCard = Environment.getExternalStorageDirectory() + "";
        apkFilePath = SDCard + "/MymenJin.apk";//文件存储路径
        //随机更新时间
        RandomTime();
        if (timer == null) {
            timer = new Timer(true);
            timer.schedule(task, 0, 1000 * 60);
        }

    }

    public void saveLog(String content) {
        String SDCard = Environment.getExternalStorageDirectory() + "";
        String fileName = SDCard + "/yunkey.txt";
        final File file = new File(fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {

            }

        }
        try {
            //打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
            FileWriter writer = new FileWriter(fileName, true);
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //获取指定范围内的随机数
    private int getRandom(int min, int max) {
        Random random = new Random();
        return random.nextInt(max) % (max - min + 1) + min;
    }

    //随机下次更新时间
    private void RandomTime() {
        /*
        更新规则：每天在固定的时间段更新（1点至5点），为了避免所有设备在同一时间更新，因此更新时间随机生成
        为了避免网络等原因造成更新失败，因此每天在（1点至5点）检测两次是否有更新
        第一次在1:00至3:59  第二次在4:00至4:59，以防止第一次更新失败
         */
        if (iUpdateHour == 4) {
            //今天第一次 1-3随机数
            iUpdateHour = getRandom(1, 3);
        } else {
            //今天第二次
            iUpdateHour = 4;
        }
        //分钟。0-59随机
        iUpdatMeinute = getRandom(0, 59);
        System.out.println("生成随机数：Hour（" + iUpdateHour + ")Meinute(" + iUpdatMeinute + ")");
        //saveLog("生成随机数：Hour（"+iUpdateHour+")Meinute("+iUpdatMeinute+")");
    }

    //是否在后台运行
    private boolean isBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(packageName)) {

                if (appProcess.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    Log.i(context.getPackageName(), "处于后台"
                            + appProcess.processName);
                    return true;
                } else {
                    Log.i(context.getPackageName(), "处于前台"
                            + appProcess.processName);
                    return false;
                }
            }
        }
        return false;
    }

    //是否运行
    private boolean runClient(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(100);
        boolean isAppRunning = false;

        for (ActivityManager.RunningTaskInfo info : list) {
            if (info.topActivity.getPackageName().equals(packageName) || info.baseActivity.getPackageName().equals(packageName)) {
                isAppRunning = true;
                //Log.i(TAG,info.topActivity.getPackageName() + " info.baseActivity.getPackageName()="+info.baseActivity.getPackageName());
                break;
            }
        }
        return isAppRunning;
    }

    //是否安装
    private boolean isAvilible() {
        final PackageManager packageManager = getApplicationContext().getPackageManager();
        // 获取所有已安装程序的包信息
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        for (int i = 0; i < pinfo.size(); i++) {
            if (pinfo.get(i).packageName.equalsIgnoreCase(packageName))
                return true;
        }
        return false;
    }

    /*
    开启App
     */
    public boolean startAPP(String appPackageName) {
        try {
            Intent intent = this.getPackageManager().getLaunchIntentForPackage(appPackageName);
            startActivity(intent);
            return true;
        } catch (Exception e) {
            System.out.println("门禁app没有运行！");
            return false;
        }
    }

    public class LocalBinder extends Binder {
        TaskService getService() {
            return TaskService.this;
        }
    }

    /*
    检测更新线程
    */
    class myTaskThread extends Thread {
        Context context;

        public myTaskThread(Context c) {
            context = c;
        }

        private String readInStream(InputStream in) {
            Scanner scanner = new Scanner(in).useDelimiter("\\A");
            return scanner.hasNext() ? scanner.next() : "";
        }

        //获取门禁App版本号
        public int getClientAppVersion() {
            PackageManager pManager = context.getPackageManager();
            //获取手机内所有应用
            List<PackageInfo> paklist = pManager.getInstalledPackages(0);
            for (int i = 0; i < paklist.size(); i++) {
                PackageInfo pak = (PackageInfo) paklist.get(i);
                if (pak.packageName.equals(packageName)) {
                    return pak.versionCode;
                }
            }
            return 0;
        }

        /*
        获取服务器版本信息，检测是否有新版本
         */
        public void getServerInfo(String update_cmd) {
System.out.println("进入检测更新。。.......");
            try {
                URL url = new URL(serverURL);
                try {
                    HttpURLConnection conn = (HttpURLConnection) url
                            .openConnection();
                    InputStream is = conn.getInputStream();

                    String result = readInStream(is);
                    int sServerVersionCode=0;
                    String[] strarray = result.split("[|]");
                    if (strarray.length == 2) {
                        String sServerVersion = strarray[0];
                        try {
                        //取到服务端的版本号转换成int类型
                         sServerVersionCode= Integer.parseInt(sServerVersion.replaceAll("\\D+","").replaceAll("\r", "").replaceAll("\n", "").trim(),10);
                        		 //nteger.valueOf(sServerVersion.toString().trim());
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                        updateURL = strarray[1];
                        int sClientVersion = getClientAppVersion();
                        if(update_cmd.equals("")||update_cmd==null) {
                        	System.out.println("需要更新[当前版本：" + sClientVersion + " ,服务器版本：" + sServerVersion + " ]" + updateURL);
                            if (sClientVersion<sServerVersionCode) {
                                System.out.println("需要更新[当前版本：" + sClientVersion + " ,服务器版本：" + sServerVersion + " ]" + updateURL);
                              //  new DownloadThread().start();
                                StartDownLoading();//断点续传下载门禁app
                            } else {
                                System.out.println("暂时没有新版本");
                            }
                        }else{
                        	StartDownLoading();//断点续传
                        }
                    }

                } catch (IOException e) {
                    // TODO Auto-generated catch block

                    e.printStackTrace();
                }
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block

                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            System.out.println("开始检测更新...");
            getServerInfo("");
        }
    }
    public void StartDownLoading() {

		new Thread(){
			public void run() {
				try {
					URL url = new URL(updateURL);
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.setRequestMethod("GET");
					conn.setConnectTimeout(5000);
					int code = conn.getResponseCode();
					if (code == 200) {
						if (getFileName(updateURL) != "") {
							File files = new File(getFileName(updateURL));
							files.delete();
						}
						long size = conn.getContentLength();// 得到服务端返回的文件的大小
						System.out.println("服务器文件的大小：" + size);
						blocksize = size / threadCount;
						// 1.首先在本地创建一个大小跟服务器一模一样的空白文件。
						File file = new File(Environment.getExternalStorageDirectory(),getFileName(updateURL));
						RandomAccessFile raf = new RandomAccessFile(file, "rw");
						raf.setLength(size);
						// 2.开启若干个子线程分别去下载对应的资源。
						runningThreadCount = threadCount;
						for (int i = 1; i <= threadCount; i++) {
							long startIndex = (i - 1) * blocksize;
							long endIndex = i * blocksize - 1;
							if (i == threadCount) {
								// 最后一个线程
								endIndex = size - 1;
							}
							System.out.println("开启线程：" + i + "下载的位置：" + startIndex + "~"
									+ endIndex);
							int threadSize = (int) (endIndex - startIndex);
							//pbs.get(i-1).setMax(threadSize);
							new DownloadThread(updateURL, i, startIndex, endIndex).start();
						}
					}
					conn.disconnect();
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			};
		}.start();
		
	}
	private class DownloadThread extends Thread {
		
		private int threadId;
		private long startIndex;
		private long endIndex;
		private String path;

		public DownloadThread(String path, int threadId, long startIndex,
				long endIndex) {
			this.path = path;
			this.threadId = threadId;
			this.startIndex = startIndex;
			this.endIndex = endIndex;
		}

		@Override
		public void run() {
			try {
				// 当前线程下载的总大小
				int total = 0;
				File positionFile = new File(Environment.getExternalStorageDirectory(),getFileName(updateURL)+threadId + ".txt");
				URL url = new URL(path);
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				conn.setRequestMethod("GET");
				// 接着从上一次的位置继续下载数据
				if (positionFile.exists() && positionFile.length() > 0) {// 判断是否有记录
					FileInputStream fis = new FileInputStream(positionFile);
					BufferedReader br = new BufferedReader(
							new InputStreamReader(fis));
					// 获取当前线程上次下载的总大小是多少
					String lasttotalstr = br.readLine();
					int lastTotal = Integer.valueOf(lasttotalstr);
					System.out.println("上次线程" + threadId + "下载的总大小："
							+ lastTotal);
					startIndex += lastTotal;
					total += lastTotal;// 加上上次下载的总大小。
					fis.close();
					//存数据库。
					//_id path threadid total
				}

				conn.setRequestProperty("Range", "bytes=" + startIndex + "-"
						+ endIndex);
			
				conn.setConnectTimeout(5000);
				int code = conn.getResponseCode();
				System.out.println("code=" + code);
				InputStream is = conn.getInputStream();
				File file = new File(Environment.getExternalStorageDirectory(),getFileName(updateURL));
				RandomAccessFile raf = new RandomAccessFile(file, "rw");
				// 指定文件开始写的位置。
				raf.seek(startIndex);
				System.out.println("第" + threadId + "个线程：写文件的开始位置："
						+ String.valueOf(startIndex));
				int len = 0;
				byte[] buffer = new byte[256];
				while ((len = is.read(buffer)) != -1) {
					RandomAccessFile rf = new RandomAccessFile(positionFile,
							"rwd");
					raf.write(buffer, 0, len);
					total += len;
					rf.write(String.valueOf(total).getBytes());
					rf.close();
					//pbs.get(threadId-1).setProgress(total);
				}
				is.close();
				raf.close();

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				// 只有所有的线程都下载完毕后 才可以删除记录文件。
				synchronized (obj) {
					System.out.println("线程" + threadId + "下载完毕了");
					runningThreadCount--;
					if (runningThreadCount < 1) {
						System.out.println("所有的线程都工作完毕了。删除临时记录的文件");
						for (int i = 1; i <= threadCount; i++) {
							File f = new File(Environment.getExternalStorageDirectory(),getFileName(updateURL)+ i + ".txt");
							System.out.println(f.delete());
						}
						 new InstallThread1().start();
					}
				}

			}
		}
	
	}
	/**
	 * 获取文件的名字
	 * 
	 * @param path
	 * @return
	 */
	public static String getFileName(String path) {
		return "MymenJin";
	}
   /* 
    apk文件下载线程
     
    class DownloadThread extends Thread {
        @Override
        public void run() {
            final File file = new File(apkFilePath);
            if (file.exists()) {

                file.delete();
            }
            try {
                file.createNewFile();
            } catch (IOException e) {

            }

            try {
                URL url = new URL(updateURL);
                try {
                    HttpURLConnection conn = (HttpURLConnection) url
                            .openConnection();
                    InputStream is = conn.getInputStream();
                    FileOutputStream fos = new FileOutputStream(file);

                    conn.connect();
                    if (conn.getResponseCode() == 200) {
                        byte[] buf = new byte[256];
                        double count = 0;
                        {
                            while (count <= 100) {
                                if (is != null) {
                                    int numRead = is.read(buf);
                                    if (numRead <= 0) {
                                        break;
                                    } else {
                                        fos.write(buf, 0, numRead);
                                    }

                                } else {
                                    break;
                                }
                            }
                            fos.close();
                            System.out.println("下载完成----,准备安装");
                           // new InstallThread().start();
                            new InstallThread1().start();
                        }
                    } else {
                        System.out.println("网络下载异常：" + conn.getResponseCode());
                    }

                    conn.disconnect();
                    is.close();

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }*/
    /*Uri uri = Uri.fromFile(new File("/sdcard/temp.apk")); //这里是APK路径  
    Intent intent = new Intent(Intent.ACTION_VIEW);  
    intent.setDataAndType(uri,"application/vnd.android.package-archive");  
   startActivity(intent);  */
    class InstallThread1 extends Thread {
        @Override
        public void run() {
        	/*Uri uri = Uri.fromFile(new File("/sdcard/temp.apk")); //这里是APK路径  
        	 Intent intent = new Intent(Intent.ACTION_VIEW);  
        	 intent.setDataAndType(uri,"application/vnd.android.package-archive");  
        	startActivity(intent);  */
        	Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(new File(apkFilePath)),
                            "application/vnd.android.package-archive");
            startActivity(intent);
        }
    };
    /*
     * 
    安装Apk并自动启动
    */
    class InstallThread extends Thread {
        @Override
        public void run() {
            Process process = null;
            OutputStream out = null;
            InputStream in = null;
            try {
                // 请求root
                process = Runtime.getRuntime().exec("su");
                out = process.getOutputStream();
                // 调用安装
                out.write(("pm install -r " + apkFilePath + "\n").getBytes());
                in = process.getInputStream();
                int len = 0;
                byte[] bs = new byte[256];
                while (-1 != (len = in.read(bs))) {
                    String state = new String(bs, 0, len);
                    if (state.equals("Success\n")) {
                        System.out.println("安装成功");
                        //安装成功后的操作
                        startAPP(packageName);
                    } else {
                        System.out.println("安装失败");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (out != null) {
                        out.flush();
                        out.close();
                    }
                    if (in != null) {
                        in.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
