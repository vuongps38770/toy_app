const express=require('express');
const router = express.Router();
var userController = require('../controllers/userController')

router.post('/users', userController.createUser);
router.get('/users', userController.getUsers);
router.post('/users/validate', userController.findAccount);
router.get('/users/:id', userController.getUserById);
router.put('/users/:id', userController.updateUser);
router.delete('/users/:id', userController.deleteUser);

module.exports=router;