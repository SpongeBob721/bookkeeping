package cn.itcast.test2;

public class CostClassify {
    //分类名 主键
    public String classifyName;
    //支出：“1”收入：“0”
    public String costInOut;
    //是否显示CheckBox
    public boolean isShow;
    //是否被选中
    public boolean isChecked;

    public String getClassifyName()
    {
        return classifyName;
    }

    public void  setClassifyName(String msg)
    {
        this.classifyName = msg;
    }

    public boolean isShow()
    {
        return isShow;
    }

    public void setShow(boolean isShow)
    {
        this.isShow = isShow;
    }

    public boolean isChecked()
    {
        return isChecked;
    }

    public void setChecked(boolean isChecked)
    {
        this.isChecked = isChecked;
    }

//    public CostClassify(String classifyName,boolean isShow,boolean isChecked)
//    {
//        super();
//        this.classifyName = classifyName;
//        this.isChecked = isChecked;
//        this.isShow = isShow;
//    }
}
