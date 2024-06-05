package com.example.curs;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class TeacherHomeFragment extends Fragment {
    private TestDao testDao;
    private List<Test> testList = new ArrayList<>();
    private TestAdapter testAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        AppDatabase db = DatabaseClient.getInstance(requireContext().getApplicationContext()).getAppDatabase();
        testDao = db.testDao();

        ListView testListView = view.findViewById(R.id.test_list_view);
        testAdapter = new TestAdapter(requireContext(), testList);
        testListView.setAdapter(testAdapter);

        loadTestsFromDatabase();

        testListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Test selectedTest = testList.get(position);
                Intent intent = new Intent(getActivity(), CreateTestActivity.class);
                intent.putExtra("testId", selectedTest.id);
                startActivity(intent);
            }
        });

        return view;
    }

    private void loadTestsFromDatabase() {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                List<Test> loadedTests = testDao.getAllTests();

                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        testList.clear();
                        testList.addAll(loadedTests);
                        testAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }
}
