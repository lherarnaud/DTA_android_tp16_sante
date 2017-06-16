package fr.formation.tp16;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import fr.formation.tp16.database.datasource.DataSource;
import fr.formation.tp16.database.modele.User;

/**
 * Created by admin on 16/06/2017.
 */

public class ListeUtilisateurFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<User> utilisateurs;

    private DataSource<User> dataSource;
    private int versionDB = 3; // Permet de detruire la base de données SQLite si on incrémente la version


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        utilisateurs = new ArrayList<>();
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            if (dataSource == null) {
                dataSource = new DataSource<>(this.getContext(), User.class, versionDB);
                dataSource.open();
            }
        } catch (Exception e) {
            // Traiter le cas !
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadAllUsers();
    }

    private void loadAllUsers() {
        // On charge les données depuis la base.
        try {
            List<User> users = dataSource.readAll();
            utilisateurs.clear();
            utilisateurs.addAll(users);
        } catch (Exception e) {
            // Que faire ?
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_utilisateurs, container, false);

        recyclerView = (RecyclerView) view;
        recyclerView.setAdapter(new RecyclerView.Adapter<UtilisateurViewHolder>() {

            @Override
            public UtilisateurViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.utilisateur_list_content, parent, false);
                return new UtilisateurViewHolder(view);

            }

            @Override
            public void onBindViewHolder(UtilisateurViewHolder holder, final int position) {

                final User user = utilisateurs.get(position);

                holder.mIdView.setText(String.valueOf(user.getId()));
                holder.mNameView.setText(user.getNom());

                holder.mView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        UtilisateurFragment articleFrag = (UtilisateurFragment) getFragmentManager().findFragmentById(R.id.article_fragment);

                        if(articleFrag != null) {
                            articleFrag.updateArticleView(user);
                        }
                        else {
                            try {
                                UtilisateurFragment newFragment = new UtilisateurFragment();
                                Bundle args = new Bundle();

                                // Transformation en JSON :
                                String flux = (new Gson()).toJson(user);

                                args.putInt(UtilisateurFragment.ARG_POSITION, position);
                                args.putString(UtilisateurFragment.USER_JSON, flux);

                                newFragment.setArguments(args);

                                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                transaction.replace(R.id.fragment_container, newFragment);
                                transaction.addToBackStack(null);
                                transaction.commit();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    }
                });

            }

            @Override
            public int getItemCount() {
                return utilisateurs.size();
            }


        });

        return view;
    }

    public class UtilisateurViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        public final TextView mIdView;
        public final TextView mNameView;


        public UtilisateurViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = view.findViewById(R.id.userId);
            mNameView = view.findViewById(R.id.userName);
        }

    }
}
