package com.example.joshmitchell.noteapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Josh Mitchell on 01/01/2018.
 */

public class ViewPagerFragment extends Fragment {

    public static final String EXTRA_NOTE_ID = "todo_id";
    private UUID noteId;

    private ArrayList<Note> mNotes;

    public static ViewPagerFragment newInstance(UUID noteId){
        Bundle args = new Bundle();

        args.putSerializable(EXTRA_NOTE_ID, noteId);

        ViewPagerFragment fragment = new ViewPagerFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        noteId = (UUID) getArguments().getSerializable(EXTRA_NOTE_ID);
        mNotes = NoteModel.get(getActivity()).getTextNotes();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View v = inflater.inflate(R.layout.activity_note_list_note_pager, container, false);

        ViewPager viewPager = v.findViewById(R.id.note_view_pager);
        viewPager.setAdapter(new FragmentStatePagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                Note t = mNotes.get(position);
                return ViewNoteFragment.newInstance(t.getId());
            }

            @Override
            public int getCount() {
                return mNotes.size();
            }
        });
        for (int i = 0; i < mNotes.size(); i++){
            if (mNotes.get(i).getId().equals(noteId)) {
                viewPager.setCurrentItem(i);
                break;
            }
        }

        return v;
    }
}
