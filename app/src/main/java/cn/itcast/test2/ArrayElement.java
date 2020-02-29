package cn.itcast.test2;
import com.hedan.piechart_library.PieChartBean;
import com.hedan.piechart_library.PieChart_View;
//标识同月份中的同一类的数据（用于图表页数据的合并显示）
public class ArrayElement {
    PieChartBean a;
    String b;
    ArrayElement(PieChartBean t1,String t2)
    {
        a=t1;
        b=t2;
    }
}
