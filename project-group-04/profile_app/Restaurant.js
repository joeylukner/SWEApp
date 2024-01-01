var mongoose = require('mongoose');

// DO NOT CHANGE THE URL FOR THE DATABASE!
// Please speak to the instructor if you need to do so or want to create your own instance
mongoose.connect('mongodb://mongo.cs.swarthmore.edu:27017/group04_dingo');

var Schema = mongoose.Schema;

var restaurantSchema = new Schema({
	name: {type: String, required: true, unique: true},
	phoneNumber: Number,
    address: {type: String},
    cuisineType: {type: String, required: true},
    hours: {type: String},
    latitude: Number,
    longitude: Number,
    });

// export restaurantSchema as a class called restaurant
module.exports = mongoose.model('Restaurant', restaurantSchema);

// this is so that the names are case-insensitive
restaurantSchema.methods.standardizeName = function() {
    this.name = this.name.toLowerCase();
    return this.name;
}
