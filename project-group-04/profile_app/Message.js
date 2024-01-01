var mongoose = require('mongoose');

// DO NOT CHANGE THE URL FOR THE DATABASE!
// Please speak to the instructor if you need to do so or want to create your own instance
mongoose.connect('mongodb://mongo.cs.swarthmore.edu:27017/group04_dingo');

var Schema = mongoose.Schema;

var messageSchema = new Schema({
	sender: {type: String, required: true},
	recipient: String,
    message: String
    });

// export restaurantSchema as a class called restaurant
module.exports = mongoose.model('Message', messageSchema);

// this is so that the names are case-insensitive
messageSchema.methods.standardizeName = function() {
    this.name = this.name.toLowerCase();
    return this.name;
}
