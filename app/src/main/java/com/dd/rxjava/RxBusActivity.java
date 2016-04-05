package com.dd.rxjava;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import rx.functions.Action1;

public class RxBusActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_bus);

        getFragmentManager().beginTransaction()
                .add(R.id.demo_rxbus_frag_1, new TopFragment())
                .add(R.id.demo_rxbus_frag_2, new BottomFragment())
                .commit();
    }

    public class TopFragment extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
            View layout = inflater.inflate(R.layout.fragment_rxbus_top, container, false);
            ((Button)layout.findViewById(R.id.btn_demo_rxbus_tap)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RxBus.getRxBus("fragment").send("hello bottom!");
                }
            });
            return layout;
        }

        @Override
        public void onActivityCreated( Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            Log.d("RxBus", "TopFragment onActivityCreated");
        }
    }

    public class BottomFragment extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
            View layout = inflater.inflate(R.layout.fragment_rxbus_bottom, container, false);
            return layout;
        }

        @Override
        public void onActivityCreated( Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            Log.d("RxBus", "BottomFragment onActivityCreated");
            RxBus rxBus = RxBus.register("fragment");

            rxBus.toObserverable().subscribe(new Action1<Object>() {
                @Override
                public void call(Object s) {
                    Log.d("RxBus", "BottomFragment s:"+s);
                }
            });
        }
    }
}
