const express=require('express');
const router = express.Router();
var userController = require('../controllers/userController')

router.post('/users/create', userController.createUser);
router.get('/users', userController.getUsers);
router.post('/users/validate', userController.findAccount);
router.put('/users/:id', userController.updateUser);
router.delete('/users/:id', userController.deleteUser);
router.post('/users/validetInf', userController.validetInf);
router.post('/users/findAccountByEmail/:email', userController.findAccountByEmail);
router.post('/users/updateUser', userController.updateUser);

module.exports=router;