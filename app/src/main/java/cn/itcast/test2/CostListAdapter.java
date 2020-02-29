package cn.itcast.test2;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.view.LayoutInflaterFactory;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class CostListAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private List<CostBean> mList;
    private Context mContext;
    private LayoutInflater mInflater;

    public interface OnItemClickListener
    {
        void onItemClick(View view,int position);
        void onItemLongClick(View view,int position);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener)
    {
        this.mOnItemClickListener = listener;
    }


    public CostListAdapter(Context context, List<CostBean> list){
        this.mContext = context;
        this.mList = list;
        mInflater = LayoutInflater.from(context);
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.list_item,viewGroup,false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder,final int pos) {
        holder.tv_title.setText(mList.get(pos).costTile);
        holder.tv_in_out.setText(mList.get(pos).costInOut);
        String time = mList.get(pos).costMinute;
        if(time.length()<2)
        {
            String temp = "0";
            time = temp + time;
        }
        holder.tv_date.setText(mList.get(pos).costHour + ":" + time);
        holder.tv_cost.setText(mList.get(pos).costMoney);
        if(holder.tv_in_out.getText().equals("支出"))
        {
            holder.tv_cost.setTextColor(Color.parseColor("#333333"));
        }
        else
        {
            holder.tv_cost.setTextColor(Color.parseColor("#ff0000"));
        }


        //item点击事件回调
        if(mOnItemClickListener != null)
        {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(holder.itemView,pos);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnItemClickListener.onItemLongClick(holder.itemView,pos);
                    return false;
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public Object getItem(int position)
    {
        return position;
    }

    public long getItemId(int position)
    {
        return position;
    }

    public void addData(CostBean costBean,int pos)
    {
//        CostBean costBean = new CostBean();
//        costBean.costDate = "time";
//        costBean.costTile = "添加";
//        costBean.costInOut = "支出";
//        costBean.costMoney = "100";
        mList.add(pos,costBean);
        notifyItemInserted(pos);
    }

    public void deleteData(int pos)
    {
        mList.remove(pos);
        notifyItemRemoved(pos);
    }

    public void upgradeData(int pos)
    {
        notifyItemChanged(pos);
    }



    public View getView(int position,View convertView, ViewGroup parent)
    {
        MyViewHolder viewHolder;
        if(convertView==null)
        {
            viewHolder = new MyViewHolder(convertView);
        }
        return null;
    }
}

class MyViewHolder extends ViewHolder
{
    TextView tv_title;
    TextView tv_in_out;
    TextView tv_date;
    TextView tv_cost;
    public MyViewHolder(View view)
    {
        super(view);

        tv_title = view.findViewById(R.id.tv_title);
        tv_in_out = view.findViewById(R.id.tv_in_out);
        tv_date = view.findViewById(R.id.tv_date);
        tv_cost = view.findViewById(R.id.tv_cost);
    }
}
