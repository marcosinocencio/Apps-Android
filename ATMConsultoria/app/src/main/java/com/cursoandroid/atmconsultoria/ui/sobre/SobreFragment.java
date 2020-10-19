package com.cursoandroid.atmconsultoria.ui.sobre;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.cursoandroid.atmconsultoria.R;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class SobreFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        String descricao = "A ATM Consultoria tem como missão apoiar organizações "+
                "que desejam alcançar o sucesso atráves da excelência em gestão e "+
                "da busca pela qualidade";

        Element versao = new Element();
        versao.setTitle("Versão 1.0");

        return new AboutPage(getActivity() )
                .setImage(R.drawable.logo)
                .setDescription(descricao)

                .addGroup("Entre em contato")
                .addEmail("atendimento@atmconsultoria.com.br","Envie um e-mail")
                .addWebsite("https://www.google.com.br", "Acesse nosso site")

                .addGroup("Redes sociais")
                .addFacebook("Facebook")
                .addInstagram("Instagram")
                .addTwitter("Twitter")
                .addYoutube("Youtube")
                .addGitHub("GitHub")
                .addPlayStore("Download App")

                .addItem(versao)



                .create();

    }
}