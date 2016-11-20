package androidkejar.noteapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidkejar.noteapp.R;
import androidkejar.noteapp.database.RealmDB;
import androidkejar.noteapp.model.Note;
import androidkejar.noteapp.util.TimeUtil;

public class SaveActivity extends AppCompatActivity {
    TextView hsper,operator, result, notif;
    Button btHitung, btJumlah, btKurang, btKali, btBagi;
    EditText input1,input2;
    private EditText noteText;
    private int operation = 0;
    private double HasilAkhir = 0.0;
    private String Cek1 = "";
    private String Cek2 = "";

    private int id;

    public static void start(Context context, int id) {
        Intent intent = new Intent(context, SaveActivity.class);
        intent.putExtra(SaveActivity.class.getSimpleName(), id);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);

        input1 = (EditText) findViewById(R.id.masukan1);
        input2 = (EditText) findViewById(R.id.masukan2);
        operator = (TextView) findViewById(R.id.operan);
        result = (TextView) findViewById(R.id.hasil);
        notif = (TextView) findViewById(R.id.notifikasi);
        hsper = (TextView) findViewById(R.id.textView3);
        btHitung = (Button) findViewById(R.id.hitung);
        btJumlah = (Button) findViewById(R.id.tambah);
        btKurang = (Button) findViewById(R.id.kurang);
        btKali = (Button) findViewById(R.id.kali);
        btBagi = (Button) findViewById(R.id.bagi);






        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        noteText = (EditText) findViewById(R.id.noteText);

        id = getIntent().getExtras().getInt(SaveActivity.class.getSimpleName());

        if (id != 0) {

            Note note = new RealmDB(this).getById(Note.class, id);
            noteText.setText(String.valueOf(note.getNote()));
        }
    }



    public void addNote(String noteText) {
        Note note = new Note();
        note.setId((int) (System.currentTimeMillis()) / 1000);
        note.setNote(noteText);
        note.setDateModified(String.valueOf(TimeUtil.getUnix()));

        new RealmDB(this).add(note);
    }


    public void updateNote(int id, String noteText) {
        Note note = new Note();
        note.setId(id);
        note.setNote(noteText);
        note.setDateModified(String.valueOf(TimeUtil.getUnix()));

        new RealmDB(this).update(note);
    }


    public void deleteNote(int id) {
        new RealmDB(this).delete(Note.class, id);
    }

    private void createOrUpdate() {
        if (!TextUtils.isEmpty(noteText.getText().toString())) {
            if (id == 0) {
                addNote(noteText.getText().toString());
            } else {
                updateNote(id, noteText.getText().toString());
            }
        } else {
            return;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.menu_save, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                createOrUpdate();
                finish();
                return true;
            case R.id.menu_save:
                createOrUpdate();
                finish();
                return true;
            case R.id.menu_delete:
                if (id != 0) deleteNote(id);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void klikTambah(View V) {
        operation = 1;
        operator.setText("   +   ");
    }

    public void klikKurang(View V) {
        operation = 2;
        operator.setText("   -   ");
    }

    public void klikKali(View V) {
        operation = 3;
        operator.setText("   x   ");
    }

    public void klikBagi(View V) {
        operation = 4;
        operator.setText("   :   ");
    }

    public void klikHasil(View V) {

        Cek1 = input1.getText().toString();
        Cek2 = input1.getText().toString();

        if ((Cek1.equalsIgnoreCase("")) || (Cek2.equalsIgnoreCase(""))) {
            notif.setText("Kolom tidak boleh kosong");
        } else {
            double inputA = Double.parseDouble(input1.getText().toString());
            double inputB = Double.parseDouble(input2.getText().toString());

            switch (operation) {
                case 1:
                    HasilAkhir = inputA + inputB;
                    break;

                case 2:
                    HasilAkhir = inputA - inputB;
                    break;

                case 3:
                    HasilAkhir = inputA * inputB;
                    break;

                case 4:
                    HasilAkhir = inputA / inputB;
                    break;

                case 0:
                    notif.setText("Harap pilih operan terlebih dahulu!");
                    break;

                default:
                    notif.setText("Undescribeable Error!");
                    break;
            }

            if (operation < 1) {
                result.setText("0");
            } else {
                String hasilString = String.valueOf(HasilAkhir);
                result.setText(hasilString);
                noteText.setText( input1.getText() + " "+ operator.getText()+" "+input2.getText()+" "+hsper.getText()+" "+result.getText());
                notif.setText("Kalkulator Siap Digunakan");
            }
        }

    }

    public void clearInput1(View V) {
        input1.setText("");
        notif.setText("Kalkulator Siap Digunakan");
        operation = 0;
    }

    public void clearInput2(View V) {
        input2.setText("");
        notif.setText("Kalkulator Siap Digunakan");
        operation = 0;
    }

}


