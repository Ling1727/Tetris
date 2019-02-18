package com.example.myapplication;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by hasee on 2019/2/14.
 */

public class DialogFragment0 extends DialogFragment {
    private TextView tvTips;
    private Button bt1,bt2,bt3;
    private View.OnClickListener listener;
    private String overOrPause;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view=inflater.inflate(R.layout.dialog_layout,container,false);
        tvTips=view.findViewById(R.id.tvTips);
        bt1=view.findViewById(R.id.bt1);
        bt2=view.findViewById(R.id.bt2);
        bt3=view.findViewById(R.id.bt3);
        tvTips=view.findViewById(R.id.tvTips);
        Bundle date=getArguments();
        overOrPause=date.getString("overOrPause");
        tvTips.setText(overOrPause);
        if(overOrPause.equals("游戏结束")){
            bt1.setVisibility(View.GONE);
        }
        listener=new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Game game=(Game)getActivity();
                switch (view.getId()){
                    case R.id.bt1:
                        game.dialogClose();
                        game.setPause();
                        break;
                    case R.id.bt2:
                        game.back();
                        if(bt1.getVisibility()==View.GONE){
                            bt1.setVisibility(View.VISIBLE);
                        }
                        break;
                    case R.id.bt3:
                        game.dialogClose();
                        game.setPause();
                        game.again();
                        if(bt1.getVisibility()==View.GONE){
                            bt1.setVisibility(View.VISIBLE);
                        }
                        break;
                }
            }
        };
        bt1.setOnClickListener(listener);
        bt2.setOnClickListener(listener);
        bt3.setOnClickListener(listener);
        return view;
    }



}
