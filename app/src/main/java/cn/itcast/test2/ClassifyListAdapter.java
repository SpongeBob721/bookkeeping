package cn.itcast.test2;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.List;

public class ClassifyListAdapter extends RecyclerView.Adapter<MyViewHolder1> {

    private List<CostClassify> mList;
    private Context mContext;
    private LayoutInflater mInflater;
    private OnShowItemClickListener onShowItemClickListener;

    public interface OnItemClickListener
    {
        void onItemClick(View view, int position);
        boolean onItemLongClick(View view,int position);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener)
    {
        this.mOnItemClickListener = listener;
    }


    public ClassifyListAdapter(Context context, List<CostClassify> list){
        this.mContext = context;
        this.mList = list;
        mInflater = LayoutInflater.from(context);
    }


    @NonNull
    @Override
    public MyViewHolder1 onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.classify,viewGroup,false);
        MyViewHolder1 viewHolder = new MyViewHolder1(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder1 holder,final int pos) {
        holder.tv_title.setText(mList.get(pos).classifyName);
        final CostClassify bean = mList.get(pos);
        //是否多选状态
        if(bean.isShow())
        {
            holder.cb.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.cb.setVisibility(View.GONE);
        }

        holder.tv_title.setText(bean.getClassifyName());
        holder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    bean.setChecked(true);
                }
                else
                {
                    bean.setChecked(false);
                }
                //回调方法，将Item加入已选
                onShowItemClickListener.onShowItemClick(bean);
            }
        });
        //必须放在监听后面
        holder.cb.setChecked(bean.isChecked());

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

//                    holder.cb.setChecked(true);
//                    holder.cb.setVisibility(View.VISIBLE);
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

    public void addData(CostClassify costClassify,int pos)
    {
        mList.add(pos,costClassify);
        notifyItemInserted(pos);
    }

    public void deleteData(int pos)
    {
        mList.remove(pos);
        notifyItemRemoved(pos);
    }

    public View getView(int position,View convertView,ViewGroup parent)
    {
        MyViewHolder1 holder;
        if(convertView == null)
        {
            holder = new MyViewHolder1(convertView);
            holder.tv_title = convertView.findViewById(R.id.tv_title);
            holder.cb = convertView.findViewById(R.id.recylerView_long_checkbox);
            convertView.setTag(holder);
        }
        else
        {
            holder = (MyViewHolder1)convertView.getTag();
        }

        final CostClassify bean = mList.get(position);
        //是否多选状态
        if(bean.isShow())
        {
            holder.cb.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.cb.setVisibility(View.GONE);
        }

        holder.tv_title.setText(bean.getClassifyName());
        holder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    bean.setChecked(true);
                }
                else
                {
                    bean.setChecked(false);
                }
                //回调方法，将Item加入已选
                onShowItemClickListener.onShowItemClick(bean);
            }
        });
        //必须放在监听后面
        holder.cb.setChecked(bean.isChecked());
        return convertView;
    }

    public interface OnShowItemClickListener
    {
        public void onShowItemClick(CostClassify bean);
    }

    public void setOnShowItemClickListener(OnShowItemClickListener onShowItemClickListener){
        this.onShowItemClickListener = onShowItemClickListener;
    }
}

class MyViewHolder1 extends RecyclerView.ViewHolder
{
    TextView tv_title;
    CheckBox cb;
    public MyViewHolder1(View view)
    {
        super(view);
        tv_title = view.findViewById(R.id.tv_title);
        cb = view.findViewById(R.id.recylerView_long_checkbox);
    }
}


