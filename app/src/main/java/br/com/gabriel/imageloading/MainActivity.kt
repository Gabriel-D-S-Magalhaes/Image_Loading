package br.com.gabriel.imageloading

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val SELECT_PICTURE = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btLoadImage.setOnClickListener { pickImage() }
    }

    /**
     * Acionar o carregamento de imagem utilizando a técnica que desejamos testar (i.e. Glide ou Picasso).
     * */
    private fun loadImage() {
        // Image Loading content here ...
        val url = "https://media1.giphy.com/media/zGnnFpOB1OjMQ/giphy.gif"// LOTR Gif
        // Glide: recomendada pelo Google na própria documentação do Android.
        Glide.with(this)// inicializa a biblioteca e informa o contexto da aplicação
            .load(url)// URL da imagem
            .into(ivMain)// referência do ImageView

        /*A declaração é semelhante à do Glide, a única diferença é que não precisamos mais
        referenciar o contexto da aplicação, pois isso será obtido a partir do imageView que foi
        declarado.*/
        /*
        Picasso.get()// inicialização da biblioteca e obtemos sua instância
            .load(url)// carregamos a URL de uma imagem
            .into(ivMain)// inserimos a imagem carregada em um imageView
        */
    }

    /**
     * CARREGA IMAGEM DA GALERIA OU TIRANDO UMA NOVA.
     * */
    fun pickImage() {
        // Pick Intent
        val pickIntent = Intent(Intent.ACTION_GET_CONTENT)// Acessar conteúdo nos documentos.
        /*Para que o Android entenda que essa ação estará relacionada apenas com imagens*/
        pickIntent.type = "image/*"

        // Take Picture Intent
        // Intenção que permite o usuário tirar uma foto nova
        // NOTE: Precisamos declarar o uso desse hardware no AndroidManifest.
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        // Pick Gallery Image
        /*Intenção de acessar conteúdos direto na galeria. Passando também a URI correspondente à
        galeria, disponibilizada através da declaração MediaStore.Images.Media.EXTERNAL_CONTENT_URI.*/
        val pickGalleryImageIntent =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        // Choose Intent
        // Para exibir o menu de opções de Intents
        val pickTitle = "Select or take a new Picture"
        val chooserIntent = Intent.createChooser(pickIntent, pickTitle)
        chooserIntent.putExtra(
            Intent.EXTRA_INITIAL_INTENTS, arrayOf(
                takePictureIntent, pickGalleryImageIntent
            )// Unindo todas as declarações anteriores
        )

        // Send Intent
        // checamos se o dispositivo possui aplicativos capazes de executar as ações desejadas,
        if (chooserIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(chooserIntent, SELECT_PICTURE)
        }
    }

    /**
     * Dispatch incoming result to the correct fragment.
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // validamos o requestCode e o resultCode
        if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK)
            if (data?.extras != null)
            /*o bitmap inteiro da imagem está dentro de uma propriedade chamada data dentro do
            objeto extras .*/ ivMain.setImageBitmap(data.extras.get("data") as Bitmap)
            else if (data?.data != null) Picasso.get().load(data.data).into(ivMain)
    }
}
