package com.vcvinci.concurrent.future;

/**
 * @Auther: Administrator
 * @Date: 2018/7/17 14:15
 * @Description:
 */
public class RealData implements Data<String> {
    private String result;

    public RealData(String queryStr) {
        System.out.println("����" + queryStr + "���в�ѯ������һ���ܺ�ʱ�Ĳ���..");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("������ϣ���ȡ���");
        result = "��ѯ���";
    }

    @Override
    public String get() {
        return null;
    }
}
