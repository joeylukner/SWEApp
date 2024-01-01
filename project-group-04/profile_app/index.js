// set up Express
var express = require('express');
var app = express();

// set up BodyParser
var bodyParser = require('body-parser');
app.use(bodyParser.urlencoded({ extended: true }));

// set up EJS
app.set('view engine', 'ejs');

// import the restaurant class from Restaurant.js
var Restaurant = require('./Restaurant.js');
//import the person class from Person.js
var Person = require('./Person.js')
// import the preference class from Preference.js
var Preference = require('./Preference.js');
// import the message class from Message.js
var Message = require('./Message.js')

app.use("/getPerson", (req, res) => {
	var filter = { 'name' : req.query.name };

	Person.findOne(filter)
		.then((result) => {
		console.log(result)
		res.json(result)
		
	})
	.catch((err) => {
		
		console.log('uh oh: ' + err);
		res.json({});
	}) 
 })
// endpoint for listing all restaurants
app.use('/allrestaurants', (req, res) => {
	Restaurant.find({})
		.then((restaurants) => {
			res.render('allrestaurants', {'restaurants' : restaurants})
		})
		.catch((err) => {
			res.type('html').status(200);
		    console.log('uh oh: ' + err);
		    res.send(err);
		})
})


app.use('/populateRestaurantSpinner', (req, res) => {
	
	Restaurant.find({})
		.then((restaurants) => {
			console.log(restaurants)
			res.json(restaurants)
		})
		.catch((err) => {
		    console.log('uh oh: ' + err);
		    res.json({});
		})
})


// endpoint for listing all persons
app.use('/allpersons', (req, res) => {
	Person.find({})
		.then((persons) => {
			res.render('allpersons', {'persons' : persons})
		})
		.catch((err) => {
			res.type('html').status(200);
		    console.log('uh oh: ' + err);
		    res.send(err);
		})
})

// endpoint for creating a new restaurant
// this is the action of the "create new restaurant" form
app.use('/createrestaurant', (req, res) => {
	// construct the restaurant from the form data which is in the request BODY
	var newRestaurant = new Restaurant ({
		name: req.body.name,
		phoneNumber: req.body.phoneNumber,
		cuisineType: req.body.cuisineType,
		address: req.body.address,
		hours: req.body.hours,
		latitude: req.body.latitude,
		longitude: req.body.longitude,
	    });

	// write it to the database
	newRestaurant.save()
		.then((p) => { 
			console.log('successfully added ' + p.name + ' to the database'); 
			// use EJS to render the page that will be displayed
			res.render('newrestaurant', {'restaurant': p})
		} )
		.catch((err) => { 
			res.type('html').status(200);
		    console.log('uh oh: ' + err);
		    res.send(err);
		})
	});

// endpoint for creating a new person
// this is the action of the "create a new person" form
app.use('/createperson', (req, res) => {
	// construct the Person from the form data which is in the request BODY
	var newPerson = new Person ({
		name: req.body.name,
		age: req.body.age,
		bio: req.body.bio,
		ageRangePref: req.body.ageRangePref,
		genderPref: req.body.genderPref,
		dietaryRestrictions: req.body.dietaryRestrictions,
		diningGroupSizePref: req.body.diningGroupSizePref
	    });

	// write it to the database
	newPerson.save()
		.then((p) => { 
			
			console.log('successfully added ' + p.name + ' to the database'); 
			// use EJS to render the page that will be displayed
			res.render('newperson', {'person': p})
		} )
		.catch((err) => { 
			res.type('html').status(200);
		    console.log('uh oh: ' + err);
		    res.send(err);
		})
	});
app.use('/createpreference', (req,res) =>{
	
	var newPref = new Preference ({
		name:req.body.name,
		score:req.body.score,
		restaurant:req.body.restaurant
	})

	newPref.save()
	.then((p) => {
		console.log('successfully added ' + p.name + ' preference to the database');
		// use EJS to render the page that will be
		res.render('newpreference', {'preference' : p})
	})
})



app.use('/createmessage', (req,res) => {

	var newMessage = new Message ({
		sender:req.body.sender,
		recipient:req.body.recipient,
		message:req.body.message
	})

	newMessage.save().then((m) => {

		console.log('successfully sent message from ' + m.sender + ' to ' + m.recipient);
		// use EJS to render the page to be displayed
		res.render('newmessage', {'message' : m})

	})
})

/* the two endpoints below here are for editing a restaurant */

// this one shows the HTML form for editing the restaurant
app.use('/showRestaurantEditForm', (req, res) => {
	var filter = { 'name' : req.query.name };
	// do a query to get the info for this restaurant
	Restaurant.findOne(filter)
	.then((p) => {
		// then show the form from the EJS template
		res.render('editrestaurantform', {'restaurant' : p})
	})
	.catch((err) => {
		res.type('html').status(200);
		console.log('uh oh: ' + err);
		res.send(err);
	})
})

// this one shows the HTML form for editing the person
app.use('/showPersonEditForm', (req, res) => {
	var filter = { 'name' : req.query.name };
	// do a query to get the info for this person
	Person.findOne(filter)
	.then((p) => {
		// then show the form from the EJS template
		res.render('editpersonform', {'person' : p})
	})
	.catch((err) => {
		res.type('html').status(200);
		console.log('uh oh: ' + err);
		res.send(err);
	})
})

app.use('/showPreferenceEditForm', (req, res) => {
	var filter = { 'name' : req.query.name,'restaurant' : req.query.restaurant };

	// do a query to get the info for this person
	Preference.findOne(filter)
	.then((p) => {
		// then show the form from the EJS template
		res.render('editpreferenceform', {'preference' : p})
		
	})
	.catch((err) => {
		res.type('html').status(200);
		console.log('uh oh: ' + err);
		res.send(err);
	})
})

// this endpoint is called when the user SUBMITS the form to edit a restaurant
app.use('/editrestaurant', (req, res) => {
	// get the name and age from the BODY of the request
	var filter = { 'name' : req.body.name };
	var update = {'phoneNumber' : req.body.phoneNumber, 'cuisineType' : req.body.cuisineType, 'address': req.body.address, 'hours' : req.body.hours, 'latitude': req.body.latitude, 'longitude': req.body.longitude}
	var updatePhoneNumber = { 'phoneNumber' : req.body.phoneNumber } ;
	var updateCuisineType = { 'cuisineType' : req.body.cuisineType } ;
	var updateAddress = {'address': req.body.address};
	var updateHours = {'hours' : req.body.hours};
	var updateLatitude = {'latitude': req.body.latitude};
	var updateLongtude = {'longitude': req.body.longitude};


	// now update the restaurant in the database
	Restaurant.findOneAndUpdate(filter, update)
	.then((orig) => { // 'orig' refers to the original object before we updated it
		res.render('editedrestaurant', {'name' : req.body.name, 'phoneNumber' : req.body.phoneNumber, 'cuisineType' : req.body.cuisineType, 'address' : req.body.address, 'hours': req.body.hours, 'latitude' : req.body.latitude, 'longitude':req.body.longitude})
		// currently only saving phone number
	})
	.catch((err) => {
		res.type('html').status(200);
		console.log('uh oh: ' + err);
		res.send(err);
	})

})

app.use('/editperson', (req, res) => {
	// get the name and age from the BODY of the request
	var filter = { 'name' : req.body.name };
	var update = {'age' : req.body.age, 'bio' : req.body.bio, 'ageRangePref' : req.body.ageRangePref, 'genderPref' : req.body.genderPref, 'dietaryRestrictions' : req.body.dietaryRestrictions, 'diningGroupSizePref' : req.body.diningGroupSizePref} ;
	var updateBio = {'bio' : req.body.bio};
	var updateAgeRangePref = {'ageRangePref' : req.body.ageRangePref};
	var updateGenderPref = {'genderPref' : req.body.genderPref};
	var updateDietaryRestrictions = {'dietaryRestrictions' : req.body.dietaryRestrictions};
	var updateDiningGroupSizePref = {'diningGroupSizePref' : req.body.diningGroupSizePref};

	// now update the person in the database
	Person.findOneAndUpdate(filter, update)
	.then((orig) => { // 'orig' refers to the original object before we updated it
		res.render('editedperson', {'name' : req.body.name, 'age' : req.body.age, 'bio' : req.body.bio, 'ageRangePref' : req.body.ageRangePref, 'genderPref' : req.body.genderPref, 'dietaryRestrictions' : req.body.dietaryRestrictions, 'diningGroupSizePref' : req.body.diningGroupSizePref})
		// currently only saving age
	})
	.catch((err) => {
		res.type('html').status(200);
		console.log('uh oh: ' + err);
		res.send(err);
	})

})

//endpoint for creating a profile from the mobile app
app.use('/saveCreateApp', (req, res) => {
	// construct the Person from the form data which is in the request BODY
	var newPerson = new Person ({
		name: req.query.name,
		age: req.query.age,
		bio : req.query.bio,
		ageRangePref: req.query.ageRangePref,
    	genderPref: req.query.genderPref,
    	dietaryRestrictions: req.query.dietaryRestrictions,
    	diningGroupSizePref: req.query.diningGroupSizePref
	    });

	// write it to the database
	newPerson.save()
		.then((result) => { 
			console.log(result)
			res.json(result)
		} )
		.catch((err) => { 
			console.log('uh oh: ' + err);
			res.json({});
		})
	});

app.use('/messageCreateApp', (req,res) => {
	var newMessage = new Message ({
		sender:req.query.sender,
		recipient:req.query.recipient,
		message:req.query.message
	})

	newMessage.save()
		.then((m) => {

		console.log(m);
		// use EJS to render the page to be displayed
		res.json(m)

	}).catch((err) => { 
		console.log('uh oh: ' + err);
		
		res.json({});
	})
})	

//endpoint for saving the profile edits from the mobile app
app.use('/saveEditApp', (req, res) => {
	// get the name and age from the BODY of the request
	var filter = { 'name' : req.query.name };
	var update = {'age' : req.query.age, 'bio' : req.query.bio, 'ageRangePref' : req.query.ageRangePref, 'genderPref' : req.query.genderPref, 'dietaryRestrictions' : req.query.dietaryRestrictions, 'diningGroupSizePref' : req.query.diningGroupSizePref} ;

	// now update the person in the database
	Person.findOneAndUpdate(filter, update)
	.then((result) => { 
		console.log(result)
		res.json(result)
	})
	.catch((err) => {
		console.log('uh oh: ' + err);
		res.json({});
	})

})

//endpoint for saving the profile edits from the mobile app
app.use('/saveAddRestaurantApp', (req, res) => {
	// get the name and age from the BODY of the request
	var filter = { 'name' : req.query.name };
	var update = {'restaurantList': req.query.restaurantList} ;
	console.log(req.query.restaurantList);
	// now update the person in the database
	Person.findOneAndUpdate(filter, update)
	.then((result) => { 
		console.log(result)
		res.json(result)
	})
	.catch((err) => {
		console.log('uh oh: ' + err);
		res.json({});
	})

})

/* These are endpoints for your group to implement in Part 4 of the activity */

app.use('/viewrestaurant', (req, res) => {
	var filter = { 'name' : req.query.name };

	Restaurant.findOne(filter)
		.then((result) => {
		res.render('viewrestaurant', {'restaurant' : result, 'requestedName' : req.query.name})
		
	})
	.catch((err) => {
		res.type('html').status(200);
		console.log('uh oh: ' + err);
		res.send(err);
	}) 
})

app.use('/viewperson', (req, res) => {
	var filter = { 'name' : req.query.name };

	Person.findOne(filter)
		.then((result) => {
		res.render('viewperson', {'person' : result, 'requestedName' : req.query.name})
		
	})
	.catch((err) => {
		res.type('html').status(200);
		console.log('uh oh: ' + err);
		res.send(err);
	}) 
})
	
app.use('/deleterestaurant', (req, res) => {
	var target = { 'name' : req.query.name};
	Restaurant.deleteOne(target)
	.then((status) => {
		res.render( 'deletedrestaurant', { 'name' : req.query.name} )
	})
	.catch((err) => {
		res.type('html').status(200);
		console.log('uh oh: ' + err);
		res.send(err);
	})

})

app.use('/deleteperson', (req, res) => {
	var target = { 'name' : req.query.name};
	Person.deleteOne(target)
	.then((status) => {
		res.render( 'deletedperson', { 'name' : req.query.name} )
		
	})
	.catch((err) => {
		res.type('html').status(200);
		console.log('uh oh: ' + err);
		res.send(err);
	})

})

app.use('/home', (req, res) => {
	Restaurant.find({})
		.then((restaurants) => {
			res.render('home', {'restaurants' : restaurants})
		})
		.catch((err) => {
			res.type('html').status(200);
		    console.log('uh oh: ' + err);
		    res.send(err);
		})

})

app.use('/viewpreference', (req, res) => {
	var filter = { 'name' : req.query.name , 'restaurant' : req.query.restaurant } 

	Preference.findOne(filter)
		.then((result) => {
		res.render('viewpreference', {'preference' : result})
		
	})
	.catch((err) => {
		res.type('html').status(200);
		console.log('uh oh: ' + err);
		res.send(err);
	}) 
})

app.use('/viewpreferences', (req, res) => {
	var filter = { 'name' : req.query.name };
	 

	Preference.find(filter)
		.then((result) => {
		res.render('viewpreferences', {'preferences' : result})
		
	})
	.catch((err) => {
		res.type('html').status(200);
		console.log('uh oh: ' + err);
		res.send(err);
	}) 
})

app.use('/viewrecievedmessages', (req, res) => {
	var filter = { 'recipient' : req.query.recipient };
	 

	Message.find(filter)
		.then((result) => {
		res.render('viewrecievedmessages', {'messages' : result})
		
	})
	.catch((err) => {
		res.type('html').status(200);
		console.log('uh oh: ' + err);
		res.send(err);
	}) 
})

app.use('/viewrecievedmessagesApp', (req, res) => {
	var filter = { 'recipient' : req.query.recipient };
	 

	Message.find(filter)
		.then((result) => {
		console.log(result.toString());
		res.json(result)
		
	})
	.catch((err) => {
		console.log('uh oh: ' + err);
		res.json({});
	}) 
})

app.use('/editpreference', (req,res) => {
	

	var filter = { 'name' : req.body.name, 'restaurant' : req.body.restaurant  }
 	var update = { 'score' : req.body.score}

	// now update the person in the database
	Preference.findOneAndUpdate(filter, update).then((orig) => { 
		console.log('Updated ' + req.body.name + ' preference');
		res.render('editedpreference', {'name' : req.body.name, 'restaurant' : req.body.restaurant, 'score' : req.body.score})

	}).catch((err) => {
		res.type('html').status(200);
		console.log('uh oh: ' + err);
		res.json(err);
	
	}) 
})

app.use('/createpreferenceApp', (req,res) => {
	var newPref = new Preference ({
		name:req.query.name,
		restaurant:req.query.restaurant,
		score:req.query.score
	})

	newPref.save()
	.then((p) => {
		console.log(p.toString());
		// use EJS to render the page that will be
		res.json(p)
	})
})

app.use('/viewpreferencesApp', (req, res) => {
	var filter = { 'name' : req.query.name };
	 

	Preference.find(filter)
		.then((result) => {
		console.log(result.toString());
		res.json(result)
		
	})
	.catch((err) => {
		console.log('uh oh: ' + err);
		res.json({});
	}) 
})





/*************************************************
Do not change anything below here!
*************************************************/

app.use('/public', express.static('public'));

// this redirects any other request to the "all" endpoint
app.use('/', (req, res) => { res.redirect('/home'); } );

// this port number has been assigned to your group
var port = 3004

app.listen(port,  () => {
	console.log('Listening on port ' + port);
    });
