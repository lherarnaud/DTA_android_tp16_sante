package fr.formation.tp16;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import fr.formation.tp16.database.modele.User;

/**
 * Created by admin on 16/06/2017.
 */
public class UtilisateurFragment extends Fragment {


    final static String ARG_POSITION = "position";
    public static final String USER_JSON = "user_json";

    User mCurrentUser = null;

    TextView userId;
    TextView userName;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(savedInstanceState != null) {
            String flux = savedInstanceState.getString(USER_JSON); // Tester si pas null ;-)
            mCurrentUser = new Gson().fromJson(flux, User.class);
        }
        View view = inflater.inflate(R.layout.utilisateur_view, container, false);
        userId = (TextView) view.findViewById(R.id.view_userId);
        userName = (TextView) view.findViewById(R.id.view_userName);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Bundle args = getArguments();
        if(args != null) {
            String flux = args.getString(USER_JSON); // Tester si pas null ;-)
            User currentUser = new Gson().fromJson(flux, User.class);
            updateArticleView(currentUser);
        } else if(mCurrentUser != null) {
            updateArticleView(mCurrentUser);
        }
    }

    public void updateArticleView(User user) {
        if(userId != null && userName != null) {
            userId.setText(String.valueOf(user.getId()));
            userName.setText(user.getNom());
            mCurrentUser = user;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ARG_POSITION, new Gson().toJson(mCurrentUser));
    }
}
