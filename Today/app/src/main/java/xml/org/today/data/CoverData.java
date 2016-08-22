package xml.org.today.data;

import java.util.Map;

import xml.org.today.R;

/**
 * Created by Administrator on 2016/7/2.
 */
public class CoverData {
    private static Map<String,Integer> IMGMAP;
    private static int[] i;
    public CoverData(){
        i=new int[]{R.mipmap.a,R.mipmap.b,R.mipmap.c,R.mipmap.d,R.mipmap.e,R.mipmap.f,
                R.mipmap.g,R.mipmap.h,R.mipmap.i,R.mipmap.j,R.mipmap.k,R.mipmap.l,R.mipmap.m,R.mipmap.n,
                R.mipmap.o,R.mipmap.p,R.mipmap.q,R.mipmap.r};
    }
    public int getImg(int id){
        int mImgUrl=i[id];
        return mImgUrl;
    }
    public int getSize(){
        return i.length;
    }
}
