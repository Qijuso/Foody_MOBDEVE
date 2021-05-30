package com.mobdeve.mc02;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.gms.maps.model.Marker;

import java.util.Objects;

public class MarkerPopup extends AppCompatDialogFragment {
    private EditText inputTextName;
    private EditText inputTextNotes;
    private MarkerPopupListener listener;
    private final boolean isNew;
    private Marker selectedMarker;

    public MarkerPopup(boolean isNew) {
        this.isNew = isNew;
    }

    public MarkerPopup(boolean isNew, Marker marker) {
        this.isNew = isNew;
        this.selectedMarker = marker;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = Objects.requireNonNull(getActivity()).getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_markerpopup, null);
        inputTextName = view.findViewById(R.id.textInput_Name);
        inputTextNotes = view.findViewById(R.id.textInput_Notes);
        String title = "Marker Information ";
        if (isNew) {
            builder.setView(view)
                    .setTitle(title)
                    .setNeutralButton("Cancel", (dialog, which) -> listener.cancelPopup())
                    .setPositiveButton("Save", (dialog, which) -> {
                        String name = inputTextName.getText().toString();
                        String notes = inputTextNotes.getText().toString();
                        listener.applyNewInfo(name, notes);
                    });
        } else {
            inputTextName.setText(selectedMarker.getTitle());
            inputTextNotes.setText(selectedMarker.getSnippet());
            builder.setView(view)
                    .setTitle(title)
                    .setNeutralButton("Cancel", (dialog, which) -> listener.cancelPopup())
                    .setNegativeButton("Delete", (dialog, which) -> listener.deleteMarker(selectedMarker))
                    .setPositiveButton("Save", (dialog, which) -> {
                        String name = inputTextName.getText().toString();
                        String notes = inputTextNotes.getText().toString();
                        listener.applyEditInfo(name, notes, selectedMarker);
                    });
        }
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (MarkerPopupListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement listener");
        }
    }

    public interface MarkerPopupListener {
        void applyNewInfo(String name, String notes);

        void applyEditInfo(String name, String notes, Marker marker);

        void cancelPopup();

        void deleteMarker(Marker marker);
    }
}
