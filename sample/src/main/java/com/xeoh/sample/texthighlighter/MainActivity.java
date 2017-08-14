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
      }

      TextView nameTextView = (TextView) convertView.findViewById(R.id.name);
      nameTextView.setText(quotes.get(position).getKey());

      TextView quoteTextView = (TextView) convertView.findViewById(R.id.quote);
      quoteTextView.setText(quotes.get(position).getValue());

      textHighlighter.addTarget(nameTextView);
      textHighlighter.addTarget(quoteTextView);
      textHighlighter.invalidate(matcher);

      return convertView;
    }
  }

  private static final HashMap<String, String> QUOTES;
  static
  {
    QUOTES = new HashMap<>();
    QUOTES.put("Edsger Dijkstra",
        "If debugging is the process of removing software bugs, then programming must be the " +
            "process of putting them in.");
    QUOTES.put("Linus Torvalds", "Most good programmers do programming not because they expect " +
        "to get paid or get adulation by the public, but because it is fun to program.");
    QUOTES.put("Chris Heilmann", "Java is to JavaScript what Car is to Carpet.");
    QUOTES.put("Edward V Berard", "Walking on water and developing software from a " +
        "specification are easy if both are frozen.");
    QUOTES.put("Brian Kernighan", "Debugging is twice as hard as writing the code in the " +
        "first place. Therefore, if you write the code as cleverly as possible, you are, " +
        "by definition, not smart enough to debug it.");
    QUOTES.put("Rick Osborne", "Always code as if the guy who ends up maintaining your code " +
        "will be a violent psychopath who knows where you live.");
    QUOTES.put("Nathan Myhrvold", "Software sucks because users demand it to.");
  }
}