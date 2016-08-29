package testing


class Helper {

    public static String initialCapital(String s) {
        s.substring(0,1).toUpperCase() + s.substring(1)
    }


    /**
     * Clean up process for photos
     * this moves the deleted file from users folder to users_folder/deleted/samephoto_systemtime
     *
     * If later a requirement to reduce disk usage -- these deleted folders can be targeted with no issues
     *
     * @param path
     * @param fileName
     */
    public static void deleteFile(String path, String toPath, String fileName) {
        InputStream inStream = null
        OutputStream outStream = null
        try {
            File file1 = new File(path+fileName)
            if (file1.exists() && !file1.isDirectory()) {
                File file2 = new File(toPath)
                file2.mkdirs()
                file2 = new File(toPath + fileName + "_" + (System.currentTimeMillis() as String))
                inStream = new FileInputStream(file1)
                outStream = new FileOutputStream(file2)
                byte[] buffer = new byte[1024]
                int length;
                //copy the file content in bytes
                while ((length = inStream.read(buffer)) > 0) {
                    outStream.write(buffer, 0, length)
                }
                inStream.close()
                outStream.close()
                file1.delete()
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public static String zodiacSign(int month, int day) {
        StringBuilder sb = new StringBuilder()
        switch (month) {
            case 1:
                if (day < 20) {
                    sb.append("CAP");
                } else {
                    sb.append("AQU");
                }
                break;
            case 2:
                if (day < 18) {
                    sb.append("AQU");
                } else {
                    sb.append("PIS");
                }
                break;
            case 3:
                if (day < 21) {
                    sb.append("PIS");
                } else {
                    sb.append("ARI");
                }
                break;
            case 4:
                if (day < 20) {
                    sb.append("ARI");
                } else {
                    sb.append("TAU");
                }
                break;
            case 5:
                if (day < 21) {
                    sb.append("TAU");
                } else {
                    sb.append("GEM");
                }
                break;
            case 6:
                if (day < 21) {
                    sb.append("GEM");
                } else {
                    sb.append("CAN");
                }
                break;
            case 7:
                if (day < 23) {
                    sb.append("CAN");
                } else {
                    sb.append("LEO");
                }
                break;
            case 8:
                if (day < 23) {
                    sb.append("LEO");
                } else {
                    sb.append("VIR");
                }
                break;
            case 9:
                if (day < 23) {
                    sb.append("VIR");
                } else {
                    sb.append("LIB");
                }
                break;
            case 10:
                if (day < 23) {
                    sb.append("LIB");
                } else {
                    sb.append("SCO");
                }
                break;
            case 11:
                if (day < 22) {
                    sb.append("SCO");
                } else {
                    sb.append("SAG");
                }
                break;
            case 12:
                if (day < 22) {
                    sb.append("SAG");
                } else {
                    sb.append("CAP");
                }
                break;
        }
        return sb.toString()
    }
}
