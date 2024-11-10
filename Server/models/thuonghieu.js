const mongoose = require('mongoose')
const Schema = mongoose.Schema;


const thuonghieu = new Schema({
    tenthuonghieu: {
        type: String,
        required:true,
        unique:true,
    },
    urlthumbnail:{
        type:String,
    },
    isactive:{
        type:Number,
    }
}, {
    collection: 'thuonghieu',
    timestamps: true
})

module.exports= mongoose.model('thuonghieu',thuonghieu,'thuonghieu')