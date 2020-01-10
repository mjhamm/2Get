package com.example.outof;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class ExpandableListData {

    public static HashMap<String, ArrayList<MakeListItem>> getData() {
        LinkedHashMap<String, ArrayList<MakeListItem>> expandableListDetail = new LinkedHashMap<>();
        //Baby & Childcare
        ArrayList<MakeListItem> baby = new ArrayList<>();
        baby.add(new MakeListItem("Baby Food", false));
        baby.add(new MakeListItem("Diapers", false));
        baby.add(new MakeListItem("Formula", false));
        baby.add(new MakeListItem("Wipes", false));
        //Baking
        ArrayList<MakeListItem> baking = new ArrayList<>();
        baking.add(new MakeListItem("Flour", false));
        baking.add(new MakeListItem("Pancake Mix", false));
        baking.add(new MakeListItem("Sugar", false));
        baking.add(new MakeListItem("Vanilla", false));
        baking.add(new MakeListItem("Yeast", false));
        //Beverages
        ArrayList<MakeListItem> beverages = new ArrayList<>();
        beverages.add(new MakeListItem("Coffee", false));
        beverages.add(new MakeListItem("Juice", false));
        beverages.add(new MakeListItem("Soda", false));
        beverages.add(new MakeListItem("Tea", false));
        beverages.add(new MakeListItem("Water", false));
        //Bread
        ArrayList<MakeListItem> bread = new ArrayList<>();
        bread.add(new MakeListItem("Bagels", false));
        bread.add(new MakeListItem("Bulky Rolls", false));
        bread.add(new MakeListItem("Muffins", false));
        bread.add(new MakeListItem("Pitas", false));
        bread.add(new MakeListItem("Sandwich", false));
        bread.add(new MakeListItem("Tortilla", false));
        //Breakfast & Cereal
        ArrayList<MakeListItem> breakfast = new ArrayList<>();
        breakfast.add(new MakeListItem("Breakfast Bars", false));
        breakfast.add(new MakeListItem("Cold Cereal", false));
        breakfast.add(new MakeListItem("Granola", false));
        breakfast.add(new MakeListItem("Hot Cereal", false));
        breakfast.add(new MakeListItem("Oatmeal", false));
        //Canned Goods
        ArrayList<MakeListItem> cannedGoods = new ArrayList<>();
        cannedGoods.add(new MakeListItem("Fruit", false));
        cannedGoods.add(new MakeListItem("Pumpkin", false));
        cannedGoods.add(new MakeListItem("Soup", false));
        cannedGoods.add(new MakeListItem("Tomato Sauce", false));
        cannedGoods.add(new MakeListItem("Tuna", false));
        cannedGoods.add(new MakeListItem("Vegetables", false));
        //Condiments
        ArrayList<MakeListItem> condiments = new ArrayList<>();
        condiments.add(new MakeListItem("Jelly", false));
        condiments.add(new MakeListItem("Ketchup", false));
        condiments.add(new MakeListItem("Mayonnaise", false));
        condiments.add(new MakeListItem("Mustard", false));
        condiments.add(new MakeListItem("Peanut Butter", false));
        condiments.add(new MakeListItem("Pickles", false));
        condiments.add(new MakeListItem("Relish", false));
        //Dairy
        ArrayList<MakeListItem> dairy = new ArrayList<>();
        dairy.add(new MakeListItem("Butter", false));
        dairy.add(new MakeListItem("Cheese", false));
        dairy.add(new MakeListItem("Milk", false));
        dairy.add(new MakeListItem("Sour Cream", false));
        dairy.add(new MakeListItem("Yogurt", false));
        //Deli
        ArrayList<MakeListItem> deli = new ArrayList<>();
        deli.add(new MakeListItem("Cheese", false));
        deli.add(new MakeListItem("Ham", false));
        deli.add(new MakeListItem("Roast Beef", false));
        deli.add(new MakeListItem("Salad", false));
        deli.add(new MakeListItem("Turkey", false));
        //Frozen Foods
        ArrayList<MakeListItem> frozenFoods = new ArrayList<>();
        frozenFoods.add(new MakeListItem("Ice Cream", false));
        frozenFoods.add(new MakeListItem("Meals", false));
        frozenFoods.add(new MakeListItem("Pizza", false));
        frozenFoods.add(new MakeListItem("Potatoes", false));
        frozenFoods.add(new MakeListItem("Vegetables", false));
        frozenFoods.add(new MakeListItem("Waffles", false));
        //Health & Beauty
        ArrayList<MakeListItem> toiletries = new ArrayList<>();
        toiletries.add(new MakeListItem("Bandages", false));
        toiletries.add(new MakeListItem("Cold Medicine", false));
        toiletries.add(new MakeListItem("Conditioner", false));
        toiletries.add(new MakeListItem("Deodorant", false));
        toiletries.add(new MakeListItem("Floss", false));
        toiletries.add(new MakeListItem("Lotion", false));
        toiletries.add(new MakeListItem("Pain Relievers", false));
        toiletries.add(new MakeListItem("Razors", false));
        toiletries.add(new MakeListItem("Shampoo", false));
        toiletries.add(new MakeListItem("Shaving Cream", false));
        toiletries.add(new MakeListItem("Soap", false));
        toiletries.add(new MakeListItem("Toothpaste", false));
        toiletries.add(new MakeListItem("Vitamins", false));
        //Household
        ArrayList<MakeListItem> household = new ArrayList<>();
        household.add(new MakeListItem("Batteries", false));
        household.add(new MakeListItem("Glue", false));
        household.add(new MakeListItem("Light Bulbs", false));
        household.add(new MakeListItem("Tape", false));

        //Laundry, Paper & Cleaning
        ArrayList<MakeListItem> paperWrap = new ArrayList<>();
        paperWrap.add(new MakeListItem("Aluminum Foil", false));
        paperWrap.add(new MakeListItem("Bleach", false));
        paperWrap.add(new MakeListItem("Dishwashing Liquid", false));
        paperWrap.add(new MakeListItem("Disinfectant Wipes", false));
        paperWrap.add(new MakeListItem("Garbage Bags", false));
        paperWrap.add(new MakeListItem("Glass Cleaner", false));
        paperWrap.add(new MakeListItem("Hand Soap", false));
        paperWrap.add(new MakeListItem("Household Cleaner", false));
        paperWrap.add(new MakeListItem("Laundry Detergent", false));
        paperWrap.add(new MakeListItem("Laundry Softener", false));
        paperWrap.add(new MakeListItem("Paper Towels", false));
        paperWrap.add(new MakeListItem("Plastic Bags", false));
        paperWrap.add(new MakeListItem("Plastic Wrap", false));
        paperWrap.add(new MakeListItem("Sponges", false));
        paperWrap.add(new MakeListItem("Tissues", false));
        paperWrap.add(new MakeListItem("Toilet Paper", false));
        paperWrap.add(new MakeListItem("Trash Bags", false));
        //Meat & Fish
        ArrayList<MakeListItem> meat = new ArrayList<>();
        meat.add(new MakeListItem("Bacon", false));
        meat.add(new MakeListItem("Beef", false));
        meat.add(new MakeListItem("Fish", false));
        meat.add(new MakeListItem("Pork", false));
        meat.add(new MakeListItem("Poultry", false));
        meat.add(new MakeListItem("Sausage", false));
        //Pet Items
        ArrayList<MakeListItem> petItems = new ArrayList<>();
        petItems.add(new MakeListItem("Cat Food",false));
        petItems.add(new MakeListItem("Cat Litter",false));
        petItems.add(new MakeListItem("Dog Food",false));
        //Produce
        ArrayList<MakeListItem> produce = new ArrayList<>();
        produce.add(new MakeListItem("Apples", false));
        produce.add(new MakeListItem("Avocados", false));
        produce.add(new MakeListItem("Bananas", false));
        produce.add(new MakeListItem("Berries", false));
        produce.add(new MakeListItem("Broccoli", false));
        produce.add(new MakeListItem("Carrots", false));
        produce.add(new MakeListItem("Cucumber", false));
        produce.add(new MakeListItem("Garlic", false));
        produce.add(new MakeListItem("Grapes", false));
        produce.add(new MakeListItem("Oranges", false));
        produce.add(new MakeListItem("Lettuce", false));
        produce.add(new MakeListItem("Melons", false));
        produce.add(new MakeListItem("Mushrooms", false));
        produce.add(new MakeListItem("Onions", false));
        produce.add(new MakeListItem("Peppers", false));
        produce.add(new MakeListItem("Potatoes", false));
        produce.add(new MakeListItem("Tomatoes", false));
        produce.add(new MakeListItem("Zucchini", false));
        //Rice & Pasta
        ArrayList<MakeListItem> pasta = new ArrayList<>();
        pasta.add(new MakeListItem("Brown Rice", false));
        pasta.add(new MakeListItem("Lasagna", false));
        pasta.add(new MakeListItem("Macaroni", false));
        pasta.add(new MakeListItem("Shells", false));
        pasta.add(new MakeListItem("Spaghetti", false));
        pasta.add(new MakeListItem("White Rice", false));
        //Sauce & Oil
        ArrayList<MakeListItem> sauceOil = new ArrayList<>();
        sauceOil.add(new MakeListItem("BBQ Sauce",false));
        sauceOil.add(new MakeListItem("Maple Syrup",false));
        sauceOil.add(new MakeListItem("Oil",false));
        sauceOil.add(new MakeListItem("Salad Dressing",false));
        sauceOil.add(new MakeListItem("Soy Sauce",false));
        sauceOil.add(new MakeListItem("Spaghetti Sauce",false));
        sauceOil.add(new MakeListItem("Vinegar",false));
        //Snacks
        ArrayList<MakeListItem> snacks = new ArrayList<>();
        snacks.add(new MakeListItem("Candy",false));
        snacks.add(new MakeListItem("Chips",false));
        snacks.add(new MakeListItem("Cookies",false));
        snacks.add(new MakeListItem("Crackers",false));
        snacks.add(new MakeListItem("Dip/Salsa",false));
        snacks.add(new MakeListItem("Nuts",false));
        snacks.add(new MakeListItem("Popcorn",false));
        snacks.add(new MakeListItem("Pretzels",false));
        snacks.add(new MakeListItem("Raisins",false));
        snacks.add(new MakeListItem("Snack Bars",false));
        //Spices
        ArrayList<MakeListItem> spices = new ArrayList<>();
        spices.add(new MakeListItem("Basil",false));
        spices.add(new MakeListItem("Cinnamon",false));
        spices.add(new MakeListItem("Cumin",false));
        spices.add(new MakeListItem("Oregano",false));
        spices.add(new MakeListItem("Pepper",false));
        spices.add(new MakeListItem("Salt",false));
        //Vegetarian
        ArrayList<MakeListItem> vegetarian = new ArrayList<>();
        vegetarian.add(new MakeListItem("Almond Milk", false));
        vegetarian.add(new MakeListItem("Hummus", false));
        vegetarian.add(new MakeListItem("Soy Milk", false));
        vegetarian.add(new MakeListItem("Tofu", false));

        //Adding Lists & Groups to Expandable List
        expandableListDetail.put("Baby & Childcare", baby);
        expandableListDetail.put("Baking", baking);
        expandableListDetail.put("Beverages", beverages);
        expandableListDetail.put("Bread", bread);
        expandableListDetail.put("Breakfast & Cereal", breakfast);
        expandableListDetail.put("Canned Goods", cannedGoods);
        expandableListDetail.put("Condiments", condiments);
        expandableListDetail.put("Dairy", dairy);
        expandableListDetail.put("Deli", deli);
        expandableListDetail.put("Frozen Foods", frozenFoods);
        expandableListDetail.put("Health & Beauty", toiletries);
        expandableListDetail.put("Household", household);
        expandableListDetail.put("Laundry, Paper & Cleaning", paperWrap);
        expandableListDetail.put("Meat & Fish", meat);
        expandableListDetail.put("Pet Items", petItems);
        expandableListDetail.put("Produce", produce);
        expandableListDetail.put("Rice & Pasta",pasta);
        expandableListDetail.put("Sauces & Oils", sauceOil);
        expandableListDetail.put("Snacks", snacks);
        expandableListDetail.put("Spices", spices);
        expandableListDetail.put("Vegetarian", vegetarian);

        return expandableListDetail;
    }
}
