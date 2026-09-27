import React, { useMemo, useState } from 'react';
import { SafeAreaView, ScrollView, StatusBar, StyleSheet, Text, TextInput, TouchableOpacity, View } from 'react-native';
import recipes from './recipes.json';

type Recipe = (typeof recipes)[number];
const ink = '#23392E';
const accent = '#496B4E';
const paper = '#F8F7F2';

export default function App() {
  const [query, setQuery] = useState('');
  const [selected, setSelected] = useState<Recipe | null>(null);
  const [servings, setServings] = useState(4);
  const [saved, setSaved] = useState<string[]>([]);
  const [savedOnly, setSavedOnly] = useState(false);
  const filtered = useMemo(() => recipes.filter(recipe =>
    (!savedOnly || saved.includes(recipe.id)) &&
    `${recipe.title} ${recipe.ingredients.join(' ')}`.toLowerCase().includes(query.trim().toLowerCase())
  ), [query, saved, savedOnly]);
  const toggleSaved = (id: string) => setSaved(previous => previous.includes(id) ? previous.filter(item => item !== id) : [...previous, id]);

  return (
    <SafeAreaView style={styles.safe}>
      <StatusBar barStyle="dark-content" backgroundColor={paper} />
      <ScrollView keyboardShouldPersistTaps="handled" contentContainerStyle={styles.page}>
        <Text style={styles.eyebrow}>YOUR KITCHEN COMPANION</Text>
        <Text style={styles.title}>CookandPin</Text>
        <Text style={styles.subtitle}>Good recipes, right at your fingertips.</Text>
        {selected ? (
          <View>
            <TouchableOpacity onPress={() => setSelected(null)} accessibilityRole="button" style={styles.back}><Text style={styles.backText}>‹  All recipes</Text></TouchableOpacity>
            <View style={styles.detailCard}>
              <Text style={styles.recipeTitle}>{selected.title}</Text>
              <TouchableOpacity onPress={() => toggleSaved(selected.id)} accessibilityRole="button" style={styles.save}><Text style={styles.saveText}>{saved.includes(selected.id) ? '♥ Saved' : '♡ Save recipe'}</Text></TouchableOpacity>
              <View style={styles.divider} />
              <Text style={styles.sectionTitle}>Serving estimate</Text>
              <View style={styles.servingsRow}>
                <TouchableOpacity accessibilityRole="button" accessibilityLabel="Decrease servings" style={styles.step} onPress={() => setServings(Math.max(1, servings - 1))}><Text style={styles.stepText}>−</Text></TouchableOpacity>
                <Text style={styles.servingsNumber}>{servings} servings</Text>
                <TouchableOpacity accessibilityRole="button" accessibilityLabel="Increase servings" style={styles.step} onPress={() => setServings(Math.min(24, servings + 1))}><Text style={styles.stepText}>+</Text></TouchableOpacity>
              </View>
              <Text style={styles.note}>The original recipes do not include complete quantities. This serving count is an estimate; ingredient amounts are shown as written.</Text>
              <Text style={styles.sectionTitle}>Ingredients</Text>
              {selected.ingredients.map((ingredient, index) => <Text key={`${index}-${ingredient}`} style={styles.item}>•  {ingredient.trim()}</Text>)}
              <Text style={styles.sectionTitle}>Directions</Text>
              <Text style={styles.directions}>{selected.instructions}</Text>
            </View>
          </View>
        ) : (
          <View>
            <TextInput value={query} onChangeText={setQuery} placeholder="Search recipes or ingredients" placeholderTextColor="#849186" accessibilityLabel="Search recipes" style={styles.search} />
            <View style={styles.filterRow}>
              <TouchableOpacity accessibilityRole="button" onPress={() => setSavedOnly(false)} style={[styles.filter, !savedOnly && styles.filterActive]}><Text style={[styles.filterText, !savedOnly && styles.filterTextActive]}>All recipes</Text></TouchableOpacity>
              <TouchableOpacity accessibilityRole="button" onPress={() => setSavedOnly(true)} style={[styles.filter, savedOnly && styles.filterActive]}><Text style={[styles.filterText, savedOnly && styles.filterTextActive]}>Saved ({saved.length})</Text></TouchableOpacity>
            </View>
            <Text style={styles.listHeading}>{savedOnly ? 'Your saved recipes' : 'Explore recipes'}</Text>
            {filtered.map(recipe => (
              <TouchableOpacity key={recipe.id} accessibilityRole="button" onPress={() => {setSelected(recipe); setServings(4);}} style={styles.card}>
                <View style={styles.cardBody}><Text style={styles.cardTitle}>{recipe.title}</Text><Text numberOfLines={2} style={styles.cardDescription}>{recipe.ingredients.slice(0, 4).join(' · ')}</Text></View>
                <Text style={styles.arrow}>›</Text>
              </TouchableOpacity>
            ))}
            {filtered.length === 0 && <Text style={styles.empty}>{savedOnly ? 'No saved recipes yet. Open a recipe and tap Save.' : 'No recipes found. Try another ingredient.'}</Text>}
            <Text style={styles.footer}>Cook something good today.</Text>
          </View>
        )}
      </ScrollView>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  safe: { flex: 1, backgroundColor: paper }, page: { paddingHorizontal: 24, paddingTop: 32, paddingBottom: 48, maxWidth: 720, width: '100%', alignSelf: 'center' },
  eyebrow: { color: accent, fontSize: 11, fontWeight: '700', letterSpacing: 2 }, title: { color: ink, fontSize: 38, fontWeight: '800', marginTop: 8 }, subtitle: { color: '#657367', fontSize: 16, marginTop: 4, marginBottom: 28 },
  search: { backgroundColor: 'white', borderWidth: 1, borderColor: '#DFE5DD', borderRadius: 16, paddingHorizontal: 17, paddingVertical: 14, fontSize: 16, color: ink },
  filterRow: { flexDirection: 'row', gap: 9, marginTop: 18, marginBottom: 29 }, filter: { borderRadius: 100, borderWidth: 1, borderColor: '#D6DED5', paddingVertical: 10, paddingHorizontal: 16 }, filterActive: { backgroundColor: ink, borderColor: ink }, filterText: { color: accent, fontWeight: '600' }, filterTextActive: { color: 'white' },
  listHeading: { color: ink, fontSize: 22, fontWeight: '700', marginBottom: 15 }, card: { backgroundColor: 'white', borderRadius: 18, padding: 19, marginBottom: 12, flexDirection: 'row', alignItems: 'center', borderWidth: 1, borderColor: '#ECEFEA' }, cardBody: { flex: 1 }, cardTitle: { color: ink, fontSize: 19, fontWeight: '700' }, cardDescription: { color: '#788579', marginTop: 7, fontSize: 13, lineHeight: 19 }, arrow: { color: accent, fontSize: 30, marginLeft: 12 }, empty: { color: '#788579', paddingVertical: 28, fontSize: 15 }, footer: { textAlign: 'center', color: '#9AA59A', marginTop: 22 },
  back: { marginBottom: 18, alignSelf: 'flex-start' }, backText: { color: accent, fontSize: 16, fontWeight: '700' }, detailCard: { backgroundColor: 'white', borderRadius: 22, padding: 23, borderWidth: 1, borderColor: '#ECEFEA' }, recipeTitle: { color: ink, fontSize: 29, fontWeight: '800' }, save: { marginTop: 15, backgroundColor: '#EDF3EC', borderRadius: 12, paddingVertical: 11, paddingHorizontal: 15, alignSelf: 'flex-start' }, saveText: { color: accent, fontWeight: '700' }, divider: { height: 1, backgroundColor: '#E9EEE8', marginVertical: 24 }, sectionTitle: { color: ink, fontSize: 19, fontWeight: '700', marginTop: 22, marginBottom: 12 }, servingsRow: { flexDirection: 'row', alignItems: 'center', gap: 15 }, step: { backgroundColor: '#EDF3EC', width: 38, height: 38, borderRadius: 10, justifyContent: 'center', alignItems: 'center' }, stepText: { color: ink, fontSize: 23, lineHeight: 27 }, servingsNumber: { color: ink, fontSize: 16, minWidth: 80, textAlign: 'center' }, note: { color: '#738073', fontSize: 12, lineHeight: 19, marginTop: 12 }, item: { color: '#435347', fontSize: 15, lineHeight: 26 }, directions: { color: '#435347', fontSize: 15, lineHeight: 25 }
});
