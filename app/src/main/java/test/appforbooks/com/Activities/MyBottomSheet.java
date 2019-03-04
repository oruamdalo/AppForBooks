package test.appforbooks.com.Activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import test.appforbooks.com.MainActivity;
import test.appforbooks.com.R;

public class MyBottomSheet extends BottomSheetDialogFragment{
    private BottomSheetListener mListener;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_sheet, container, false);

        FloatingActionButton fab = v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        //TODO: set the text with correspondi book values

        //I dont need this. It was just an example
        /*Button button1 = v.findViewById(R.id.btn_button1);
        Button button2 = v.findViewById(R.id.button2);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onButtonClicked("Button 1 clicked");
                dismiss();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onButtonClicked("Button 2 clicked");
                dismiss();
            }
        });*/

        return v;
//        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.bottom_sheet, null);
        dialog.setContentView(v);

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) v.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();
        ((BottomSheetBehavior) behavior).setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    public interface BottomSheetListener{
        void onButtonClicked(String text);
    }

    @Override
    public int getTheme() {
        super.getTheme();
        return R.style.BaseBottomSheetDialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        return new BottomSheetDialog(requireContext(), getTheme());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try{
            mListener = (BottomSheetListener) context;
        }catch(Exception e) {
            throw new ClassCastException(context.toString() + " must implement BottomSheetListener");
        }


    }
}
