package com.xeoh.sample.texthighlighter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.xeoh.android.texthighlighter.TextHighlighter;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
  private static final int DEFAULT_BG_COLOR = Color.YELLOW;
  private static final int DEFAULT_FG_COLOR = Color.RED;

  private TextHighlighter textHighlighter = new TextHighlighter()
      .setBackgroundColor(DEFAULT_BG_COLOR)
      .setForegroundColor(DEFAULT_FG_COLOR);

  private TextHighlighter.Matcher matcher = TextHighlighter.CASE_INSENSITIVE_MATCHER;

  private ListView listView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    QuoteListAdapter adapter = new QuoteListAdapter(QUOTES, textHighlighter);
    listView = (ListView) findViewById(R.id.list_view);
    listView.setAdapter(adapter);

    EditText searchText = (EditText) findViewById(R.id.search_text);
    searchText.addTextChangedListener(searchTextWatcher);

    EditText fgColorText = (EditText) findViewById(R.id.fg_color_text);
    fgColorText.addTextChangedListener(fgColorTextWatcher);

    EditText bgColorText = (EditText) findViewById(R.id.bg_color_text);
    bgColorText.addTextChangedListener(bgColorTextWatcher);

    CheckBox boldText = (CheckBox) findViewById(R.id.bold_text);
    boldText.setOnCheckedChangeListener(boldChangedListener);

    CheckBox italicText = (CheckBox) findViewById(R.id.italic_text);
    italicText.setOnCheckedChangeListener(italicChangedListener);
  }

  private TextWatcher searchTextWatcher = new TextWatcher() {
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
      String text = s.toString();

      if (text.isEmpty()) {
        QuoteListAdapter adapter = new QuoteListAdapter(QUOTES, textHighlighter);
        listView.setAdapter(adapter);
      } else {
        HashMap<String, String> filtered = new HashMap<>();

        for (HashMap.Entry<String, String> entry : QUOTES.entrySet()) {
          if (matcher.isHighlightable(entry.getKey(), text)
              || matcher.isHighlightable(entry.getValue(), text)) {
            filtered.put(entry.getKey(), entry.getValue());
          }
        }
        QuoteListAdapter adapter = new QuoteListAdapter(filtered, textHighlighter);
        listView.setAdapter(adapter);
      }

      textHighlighter.highlight(s.toString(), matcher);
    }

    @Override
    public void afterTextChanged(Editable s) { }
  };

  private TextWatcher fgColorTextWatcher = new TextWatcher() {
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
      if (s.toString().isEmpty()) {
        textHighlighter.setForegroundColor(DEFAULT_FG_COLOR);
      } else {
        try {
          int color = Color.parseColor("#" + s.toString());
          textHighlighter.setForegroundColor(color);
        } catch (IllegalArgumentException iae) {
          textHighlighter.resetForegroundColor();
        }
      }
      textHighlighter.invalidate(matcher);
    }

    @Override
    public void afterTextChanged(Editable s) { }
  };

  private TextWatcher bgColorTextWatcher = new TextWatcher() {
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
      if (s.toString().isEmpty()) {
        textHighlighter.setBackgroundColor(DEFAULT_BG_COLOR);
      } else {
        try {
          int color = Color.parseColor("#" + s.toString());
          textHighlighter.setBackgroundColor(color);
        } catch (IllegalArgumentException iae) {
          textHighlighter.resetBackgroundColor();
        }
      }
      textHighlighter.invalidate(matcher);
    }

    @Override
    public void afterTextChanged(Editable s) { }
  };

  private OnCheckedChangeListener boldChangedListener = new OnCheckedChangeListener() {
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
      textHighlighter.setBold(isChecked);
      textHighlighter.invalidate(matcher);
    }
  };

  private OnCheckedChangeListener italicChangedListener = new OnCheckedChangeListener() {
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
      textHighlighter.setItalic(isChecked);
      textHighlighter.invalidate(matcher);
    }
  };

  private class QuoteListAdapter extends BaseAdapter {
    private ArrayList<HashMap.Entry<String, String>> quotes;
    private TextHighlighter textHighlighter;

    QuoteListAdapter(HashMap<String, String> quotes, TextHighlighter textHighlighter) {
      this.quotes = new ArrayList<>(quotes.entrySet());
      this.textHighlighter = textHighlighter;
    }

    @Override
    public int getCount() {
      return quotes.size();
    }

    @Override
    public Object getItem(int position) {
      return quotes.get(position);
    }

    @Override
    public long getItemId(int position) {
      return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      Context context = parent.getContext();

      if (convertView == null) {
        LayoutInflater inflater =
            (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.list_view_item, parent, false);
        TextView nameTextView = (TextView) convertView.findViewById(R.id.name);
        TextView quoteTextView = (TextView) convertView.findViewById(R.id.quote);
        textHighlighter.addTarget(nameTextView);
        textHighlighter.addTarget(quoteTextView);
      }

      TextView nameTextView = (TextView) convertView.findViewById(R.id.name);
      TextView quoteTextView = (TextView) convertView.findViewById(R.id.quote);
      nameTextView.setText(quotes.get(position).getKey());
      quoteTextView.setText(quotes.get(position).getValue());
      textHighlighter.invalidate(matcher);

      return convertView;
    }
  }

  private static final HashMap<String, String> QUOTES;
  static
  {
    QUOTES = new HashMap<>();
    for (int i = 0; i < 100; i++) {
      QUOTES.put("Edsger Dijkstra" + Integer.toString(i),
              "If debugging is the process of removing software bugs, then programming must be the " +
                      "process of putting them in.");
      QUOTES.put("Linus Torvalds" + Integer.toString(i),
              "Most good programmers do programming not because they expect " +
              "to get paid or get adulation by the public, but because it is fun to program.");
      QUOTES.put("Chris Heilmann" + Integer.toString(i),
              "Java is to JavaScript what Car is to Carpet.");
      QUOTES.put("Edward V Berard" + Integer.toString(i),
              "Walking on water and developing software from a " +
              "specification are easy if both are frozen.");
      QUOTES.put("Brian Kernighan" + Integer.toString(i),
              "Debugging is twice as hard as writing the code in the " +
              "first place. Therefore, if you write the code as cleverly as possible, you are, " +
              "by definition, not smart enough to debug it.");
      QUOTES.put("Rick Osborne" + Integer.toString(i),
              "Always code as if the guy who ends up maintaining your code " +
              "will be a violent psychopath who knows where you live.");
      QUOTES.put("Nathan Myhrvold" + Integer.toString(i),
              "Software sucks because users demand it to.");
    }
  }
}